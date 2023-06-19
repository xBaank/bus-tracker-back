import arrow.core.getOrElse
import busTrackerApi.startUp
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test
import simpleJson.JsonArray
import simpleJson.JsonObject
import simpleJson.deserialized
import simpleJson.get
import utils.TestBase

const val busStopCode = "08242"

class StopsRoutingTests : TestBase {

    @Test
    fun `should get stop times`() = testApplication {
        application { startUp() }
        val response = client.get("/v1/bus/stops/$busStopCode/times")
        val body = response.bodyAsText().deserialized()

        response.status.isSuccess().shouldBe(true)
        body.getOrElse { throw it }.shouldBeInstanceOf<JsonObject>()
        body["data"].getOrElse { throw it }.shouldBeInstanceOf<JsonArray>()
    }


    @Test
    fun `should get stop times cached`() = testApplication {
        application { startUp() }
        val response = client.get("/v1/bus/stops/$busStopCode/times")
        val responseCached = client.get("/v1/bus/stops/$busStopCode/times/cached")

        responseCached.status.isSuccess().shouldBe(true)
        response.status.isSuccess().shouldBe(true)

        val body = response.bodyAsText().deserialized()
        val bodyCached = responseCached.bodyAsText().deserialized()

        body.getOrElse { throw it }.shouldBeInstanceOf<JsonObject>()
        bodyCached.getOrElse { throw it }.shouldBeInstanceOf<JsonObject>()
        body["data"].getOrElse { throw it }.shouldBeInstanceOf<JsonArray>()
        bodyCached["data"].getOrElse { throw it }.shouldBeInstanceOf<JsonArray>()
        body["lastTime"].getOrElse { throw it } shouldBeEqualTo bodyCached["lastTime"].getOrElse { throw it }
    }


    @Test
    fun `should not get stop times`() = testApplication {
        application { startUp() }
        val response = client.get("/v1/bus/stops/aasdsad/times")
        response.status shouldBeEqualTo HttpStatusCode.NotFound
    }
}