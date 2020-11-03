package dev.antoinechalifour.newsletter.acceptance

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import dev.antoinechalifour.newsletter.IntegrationTest
import dev.antoinechalifour.newsletter.infrastructure.database.NewsletterConfigurationRepository
import dev.antoinechalifour.newsletter.infrastructure.database.NewsletterRepository
import dev.antoinechalifour.newsletter.infrastructure.database.RecipientRepository
import org.springframework.beans.factory.annotation.Autowired
import retrofit2.Call
import retrofit2.Response

abstract class AcceptanceTest : IntegrationTest() {
    @Autowired
    protected lateinit var recipientRepository: RecipientRepository

    @Autowired
    protected lateinit var newsletterConfigurationRepository: NewsletterConfigurationRepository

    @Autowired
    protected lateinit var newsletterRepository: NewsletterRepository

    protected fun cleanupDatabase() {
        newsletterConfigurationRepository.deleteAll()
        recipientRepository.deleteAll()
        newsletterRepository.deleteAll()
    }

    protected fun <T> anHttpCallStub(response: T): Call<T> {
        val call: Call<T> = mock()
        whenever(call.execute()).thenReturn(Response.success(response))

        return call
    }
}
