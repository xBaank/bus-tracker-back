import arrow.core.getOrElse
import io.ktor.client.statement.*
import io.ktor.http.*
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import simpleJson.JsonArray
import simpleJson.asArray
import simpleJson.deserialized
import utils.*

const val interUrbanCode = "8__450___"
const val urbanCode = "9__1__074_"

class BusLinesTests : TestBase {
    @ParameterizedTest
    @ValueSource(strings = [interUrbanCode, urbanCode])
    fun `should get interurban line location`(code: String) = testApplicationBusTracker {
        val response = getLineLocation(code)
        val json = response.bodyAsText().deserialized().asArray().getOrElse { throw it }
        response.status.shouldBe(HttpStatusCode.OK)
        json.shouldBeInstanceOf<JsonArray>()
    }

    @Test
    fun `should not get urban line location`() = testApplicationBusTracker {
        val response = getLineLocation("asdasd")
        response.status.shouldBe(HttpStatusCode.NotFound)
    }

    @ParameterizedTest
    @ValueSource(strings = [interUrbanCode, urbanCode])
    fun `should get interUrban stops from line`(code: String) = testApplicationBusTracker {
        val response = getStops(code)
        response.status.shouldBe(HttpStatusCode.OK)
    }

    @Test
    fun `should not get stops from line`() = testApplicationBusTracker {
        val response = getStops("asdasd")
        response.status.shouldBe(HttpStatusCode.NotFound)
    }

    @ParameterizedTest
    @ValueSource(strings = [interUrbanCode, urbanCode])
    fun `should get interUrban itineraries from line`(code: String) = testApplicationBusTracker {
        val response = getItineraries(code)
        response.status.shouldBe(HttpStatusCode.OK)
    }

    @Test
    fun `should not get itineraries from line`() = testApplicationBusTracker {
        val response = getItineraries("asdasd")
        response.status.shouldBe(HttpStatusCode.NotFound)
    }
}