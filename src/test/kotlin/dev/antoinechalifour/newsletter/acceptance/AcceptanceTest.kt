package dev.antoinechalifour.newsletter.acceptance

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import dev.antoinechalifour.newsletter.infrastructure.database.NewsletterConfigurationRepository
import dev.antoinechalifour.newsletter.infrastructure.database.NewsletterRepository
import org.springframework.beans.factory.annotation.Autowired
import retrofit2.Call
import retrofit2.Response

abstract class AcceptanceTest {

    @Autowired
    protected lateinit var newsletterConfigurationRepository: NewsletterConfigurationRepository

    @Autowired
    protected lateinit var newsletterRepository: NewsletterRepository

    protected fun cleanupDatabase() {
        newsletterRepository.deleteAll()
        newsletterConfigurationRepository.deleteAll()
    }

    protected fun <T> anHttpCallStub(response: T): Call<T> {
        val call: Call<T> = mock()
        whenever(call.execute()).thenReturn(Response.success(response))

        return call
    }
}
