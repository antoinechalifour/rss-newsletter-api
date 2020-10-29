package dev.antoinechalifour.newsletter.acceptance

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.antoinechalifour.newsletter.NewsletterConfigurationTestBuilder.Companion.aNewsletterConfiguration
import dev.antoinechalifour.newsletter.SourceTestBuilder.Companion.aSource
import dev.antoinechalifour.newsletter.asTestResourceFileContent
import dev.antoinechalifour.newsletter.basicAuth
import dev.antoinechalifour.newsletter.domain.NewsletterConfiguration
import dev.antoinechalifour.newsletter.infrastructure.adapter.NewsletterConfigurationDatabaseAdapter
import dev.antoinechalifour.newsletter.infrastructure.adapter.NewsletterDatabaseAdapter
import dev.antoinechalifour.newsletter.infrastructure.http.mjml.MjmlResponse
import dev.antoinechalifour.newsletter.infrastructure.http.mjml.MjmlService
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.simplejavamail.api.mailer.Mailer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

@SpringBootTest
@AutoConfigureMockMvc
class SendNewsletterAcceptanceTest : AcceptanceTest() {
    private lateinit var newsletterConfiguration: NewsletterConfiguration
    private var mockWebServer = MockWebServer()

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var newsletterDatabaseAdapter: NewsletterDatabaseAdapter

    @Autowired
    private lateinit var newsletterConfigurationDatabaseAdapter: NewsletterConfigurationDatabaseAdapter

    @MockBean
    private lateinit var mailer: Mailer

    @MockBean
    private lateinit var mjmlService: MjmlService

    @BeforeEach
    fun setup() {
        cleanupDatabase()

        val response = MockResponse().setBody("/test-http/rss-feed.xml".asTestResourceFileContent())
        mockWebServer.start()
        mockWebServer.enqueue(response)

        val aSource = aSource().withUrl(mockWebServer.url("/source.xml").toString())
        newsletterConfiguration = aNewsletterConfiguration().withSources(aSource).build()
        newsletterConfigurationDatabaseAdapter.save(newsletterConfiguration)

        val mjmlResponse = anHttpCallStub(MjmlResponse("some html"))
        whenever(mjmlService.render(any())).thenReturn(mjmlResponse)
    }

    @AfterEach
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `sends the newsletter and saves it`() {
        // When
        mockMvc.post("/api/v1/newsletter-configuration/${newsletterConfiguration.id}/newsletter") {
            basicAuth("admin", "passwd")
        }

        // Then
        val newsletters =
            newsletterDatabaseAdapter.ofNewsletterConfigurationId(newsletterConfiguration.id)

        assertThat(newsletters[0].newsletterConfigurationId).isEqualTo(newsletterConfiguration.id)
        verify(mailer).sendMail(any())
    }

    @TestConfiguration
    class SendNewsletterAcceptanceTestConfiguration {
        @Bean
        fun clock() = Clock.fixed(
            Instant.parse("2004-10-19T12:30:00.00Z"),
            ZoneId.of("Europe/Paris")
        )
    }
}
