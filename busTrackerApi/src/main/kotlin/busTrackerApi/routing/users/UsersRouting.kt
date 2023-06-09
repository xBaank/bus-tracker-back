package busTrackerApi.routing.users

import arrow.core.Either
import arrow.core.getOrElse
import busTrackerApi.*
import busTrackerApi.config.*
import com.auth0.jwt.JWTVerifier
import com.toxicbakery.bcrypt.Bcrypt
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.ktor.ext.inject
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.updateOne
import org.litote.kmongo.eq
import org.simplejavamail.api.mailer.Mailer
import org.simplejavamail.email.EmailBuilder
import simpleJson.asString
import simpleJson.deserialized
import simpleJson.get
import java.net.URLEncoder


fun Route.authRouting() {
    val userRepo by inject<CoroutineDatabase>()
    val authSigner by inject<Signer>(AuthSignerQualifier)
    val resetPasswordSigner by inject<Signer>(ResetPasswordSignerQualifier)
    val registerSigner by inject<Signer>(RegisterSignerQualifier)
    val verifier by inject<JWTVerifier>()
    val mailer by inject<Mailer>()

    post("/register") {
        val user = call.receiveText().deserialized()
        val backUrl = call.request.queryParameters["backUrl"]?.also { URLEncoder.encode(it, "utf-8") }
            ?: return@post badRequest("Missing backUrl")
        val redirectUrl = call.request.queryParameters["redirectUrl"]?.also { URLEncoder.encode(it, "utf-8") }
            ?: return@post badRequest("Missing redirectUrl")

        val userTyped = User(
            username = user["username"].asString()
                .validateUsername()
                .getOrElse { return@post badRequest(it.message) },
            password = user["password"].asString()
                .validatePassword()
                .map { Bcrypt.hashAsString(it, saltRounds) }
                .getOrElse { return@post badRequest(it.message) },
            email = user["email"].asString()
                .validateMail()
                .getOrElse { return@post badRequest(it.message) },
            verified = false
        )

        val userExists = userRepo.getCollection<User>().findOne(User::email eq userTyped.email) != null
        if (userExists) conflict("User already exists")

        userRepo.getCollection<User>().insertOne(userTyped)

        val rawToken = registerSigner { withClaim("email", userTyped.email) }

        val token = URLEncoder.encode(rawToken, "utf-8")
        val verifyUrl = "${backUrl}/v1/users/verify?token=$token&redirectUrl=$redirectUrl"

        val email = EmailBuilder.startingBlank()
            .from("BusTracker", "noreply@bustracker.com")
            .to(userTyped.username, userTyped.email)
            .withSubject("Account Verification")
            .withHTMLText(createVerifyTemplate(userTyped.username, verifyUrl))
            .buildEmail()

        CoroutineScope(Dispatchers.IO).launch { mailer.sendMail(email) }

        call.respond(HttpStatusCode.Created)
    }

    get("/verify") {
        val redirectUrl = call.request.queryParameters["redirectUrl"] ?: return@get badRequest("Missing redirectUrl")
        val rawToken = call.request.queryParameters["token"] ?: return@get badRequest("Token not found")

        val token = Either.catch { verifier.verify(rawToken) }.getOrElse { return@get unauthorized("Invalid token") }
        val scope = token.getClaim("scope").asString() ?: return@get badRequest("Scope not found")
        if (scope != RegisterScope) return@get badRequest("Invalid scope")
        val email = token.getClaim("email").asString() ?: return@get badRequest("Email not found")

        val user = userRepo.getCollection<User>()
            .findOne(User::email eq email)
            ?: return@get badRequest("User not found")

        if (user.verified) return@get badRequest("User already verified")

        userRepo.getCollection<User>().updateOne(user.copy(verified = true))

        call.respondRedirect(redirectUrl)
    }



    post("/login") {
        val user = call.receiveText().deserialized()
        val email = user["email"].asString().getOrElse { return@post badRequest(it.message) }
        val password = user["password"].asString().getOrElse { return@post badRequest(it.message) }

        val userTyped =
            userRepo.getCollection<User>().findOne(User::email eq email) ?: return@post notFound("User not found")

        if (!userTyped.verified) badRequest("User not verified")
        if (!Bcrypt.verifyHash(password, userTyped.password)) unauthorized("Wrong password")

        val rawToken = authSigner { withClaim("email", userTyped.email) }

        val token = URLEncoder.encode(rawToken, "utf-8")
        val tokenObject = accessTokenObject(token)

        call.respondText(tokenObject, ContentType.Application.Json, HttpStatusCode.OK)
    }

    post("/send-reset-password") {
        val user = call.receiveText().deserialized()
        val email = user["email"].asString().getOrElse { return@post badRequest(it.message) }
        val redirectUrl = call.request.queryParameters["redirectUrl"]?.also { URLEncoder.encode(it, "utf-8") }
            ?: return@post badRequest("Missing redirectFrontUrl")

        val userTyped =
            userRepo.getCollection<User>().findOne(User::email eq email) ?: return@post notFound("User not found")

        val rawToken = resetPasswordSigner { withClaim("email", userTyped.email) }

        val token = URLEncoder.encode(rawToken, "utf-8")
        val redirectUrlWithToken = "$redirectUrl?token=$token"

        val emailToSend = EmailBuilder.startingBlank()
            .from("BusTracker", "noreply@bustracker.com")
            .to(userTyped.username, userTyped.email)
            .withSubject("Reset Password")
            .withHTMLText(createResetPasswordEmailTemplate(userTyped.username, redirectUrlWithToken))
            .buildEmail()

        CoroutineScope(Dispatchers.IO).launch { mailer.sendMail(emailToSend) }

        call.respond(HttpStatusCode.OK)
    }

    put("/reset-password") {
        val token = call.request.queryParameters["token"] ?: return@put badRequest("Token not found")
        val rawToken = Either.catch { verifier.verify(token) }.getOrElse { return@put unauthorized("Invalid token") }
        val email = rawToken.getClaim("email").asString() ?: return@put badRequest("Email not found")

        val scope = rawToken.getClaim("scope").asString() ?: return@put badRequest("Scope not found")
        if (scope != ResetPasswordScope) return@put badRequest("Invalid scope")

        val newPass =
            call.receiveText().deserialized()["password"].asString().validatePassword()
                .getOrElse { return@put badRequest(it.message) }

        val userTyped =
            userRepo.getCollection<User>().findOne(User::email eq email) ?: return@put notFound("User not found")

        val user = userTyped.copy(password = Bcrypt.hashAsString(newPass, saltRounds))
        userRepo.getCollection<User>().updateOne(user)

        call.respond(HttpStatusCode.OK)
    }
}


