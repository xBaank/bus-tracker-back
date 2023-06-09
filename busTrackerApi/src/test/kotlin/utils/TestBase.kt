package utils

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.core.context.GlobalContext
import org.litote.kmongo.reactivestreams.KMongo
import utils.MongoContainer.mongoDBContainer

interface TestBase {
    @BeforeEach
    fun setUp() {
        KMongo.createClient(mongoDBContainer.connectionString).getDatabase("test").drop()
        System.setProperty("MONGO_CONNECTION_STRING", mongoDBContainer.connectionString)
        System.setProperty("MONGO_DATABASE_NAME", "test")
    }

    @AfterEach
    fun tearDown() {
        GlobalContext.stopKoin()
    }
}