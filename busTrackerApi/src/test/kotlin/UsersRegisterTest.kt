import busTrackerApi.errorObject
import io.github.serpro69.kfaker.faker
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import simpleJson.jObject
import simpleJson.serialized
import utils.TestBase
import utils.getFakerUserData
import utils.register
import utils.testApplicationBusTracker

class UsersRegisterTest : TestBase {

    @Test
    fun `should register`() = testApplicationBusTracker {
        val (mail, username, password) = getFakerUserData()

        val response = register(mail, username, password)

        response.status.shouldBe(HttpStatusCode.Created)
    }

    @Test
    fun `should not register`() = testApplicationBusTracker {
        val (_, username, password) = getFakerUserData()

        val response = register("", username, password)

        response.status.shouldBe(HttpStatusCode.BadRequest)
        response.bodyAsText().shouldBeEqualTo(errorObject("Invalid mail"))
    }

    @Test
    fun `should not register with missing redirect`() = testApplicationBusTracker {
        val (mail, username, password) = getFakerUserData()

        val response = client.post("/v1/users/register?backUrl=http://localhost:8080") {
            contentType(ContentType.Application.Json)
            setBody(jObject {
                "email" += mail
                "username" += username
                "password" += password
            }.serialized())
        }

        response.status.shouldBe(HttpStatusCode.BadRequest)
        response.bodyAsText().shouldBeEqualTo(errorObject("Missing redirectUrl"))
    }

    @Test
    fun `should not register with missing backUrl`() = testApplicationBusTracker {
        val (mail, username, password) = getFakerUserData()

        val response = client.post("/v1/users/register?redirectUrl=http://localhost:8080") {
            contentType(ContentType.Application.Json)
            setBody(jObject {
                "email" += mail
                "username" += username
                "password" += password
            }.serialized())
        }

        response.status.shouldBe(HttpStatusCode.BadRequest)
        response.bodyAsText().shouldBeEqualTo(errorObject("Missing backUrl"))
    }

    @Test
    fun `should not register already existing`() = testApplicationBusTracker {
        val faker = faker {}
        val mail = faker.internet.safeEmail()
        val username = faker.name.name()
        val password = faker.crypto.md5()

        register(mail, username, password)


        val response = register(mail, username, password)
        response.status.shouldBe(HttpStatusCode.Conflict)
    }
}