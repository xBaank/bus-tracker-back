import arrow.core.getOrElse
import io.ktor.client.statement.*
import io.ktor.http.*
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test
import simpleJson.JsonArray
import simpleJson.JsonObject
import simpleJson.deserialized
import simpleJson.get
import utils.TestBase
import utils.getAbono
import utils.testApplicationBusTracker

const val abonoJoven = "0000000010040117584"
const val abonoAdulto = "2222222510010656361"

class AbonoTest : TestBase {
    @Test
    fun `should get abono joven`() = testApplicationBusTracker {
        val result = getAbono(abonoJoven)

        val json = result.bodyAsText().deserialized().getOrElse { throw it }

        result.status.shouldBe(HttpStatusCode.OK)
        json.shouldBeInstanceOf<JsonObject>()
        json["contracts"].getOrElse { throw it }.shouldBeInstanceOf<JsonArray>()
    }

    @Test
    fun `should get abono adulto`() = testApplicationBusTracker {
        val result = getAbono(abonoAdulto)

        val json = result.bodyAsText().deserialized().getOrElse { throw it }

        result.status.shouldBe(HttpStatusCode.OK)
        json.shouldBeInstanceOf<JsonObject>()
        json["contracts"].getOrElse { throw it }.shouldBeInstanceOf<JsonArray>()
    }

    @Test
    fun `should not get abono`() = testApplicationBusTracker {
        val result = getAbono("asdasd")
        result.status.shouldBe(HttpStatusCode.NotFound)
    }
}