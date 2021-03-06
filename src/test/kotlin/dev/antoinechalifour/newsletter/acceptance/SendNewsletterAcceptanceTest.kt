package dev.antoinechalifour.newsletter.acceptance

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.check
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.antoinechalifour.newsletter.NewsletterConfigurationTestBuilder.Companion.aNewsletterConfiguration
import dev.antoinechalifour.newsletter.RecipientTestBuilder.Companion.aRecipient
import dev.antoinechalifour.newsletter.SourceTestBuilder.Companion.aSource
import dev.antoinechalifour.newsletter.asTestResourceFileContent
import dev.antoinechalifour.newsletter.bearerToken
import dev.antoinechalifour.newsletter.domain.NewsletterConfiguration
import dev.antoinechalifour.newsletter.domain.NewsletterConfigurationPort
import dev.antoinechalifour.newsletter.domain.NewsletterPort
import dev.antoinechalifour.newsletter.domain.Recipient
import dev.antoinechalifour.newsletter.domain.RecipientPort
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
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
class SendNewsletterAcceptanceTest : AcceptanceTest() {

    private val authorizedToken = "my-token"
    private lateinit var theRecipient: Recipient
    private lateinit var theNewsletterConfiguration: NewsletterConfiguration
    private var mockWebServer = MockWebServer()

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var recipientPort: RecipientPort

    @Autowired
    private lateinit var newsletterPort: NewsletterPort

    @Autowired
    private lateinit var newsletterConfigurationPort: NewsletterConfigurationPort

    @MockBean
    private lateinit var mailer: Mailer

    @MockBean
    private lateinit var mjmlService: MjmlService

    @BeforeEach
    fun setup() {
        cleanupDatabase()
        authorizeForToken(authorizedToken)

        val response = MockResponse().setBody("/test-http/rss-feed.xml".asTestResourceFileContent())
        mockWebServer.start()
        mockWebServer.enqueue(response)

        val aSource = aSource().withUrl(mockWebServer.url("/source.xml").toString())
        val recipientId = UUID.randomUUID()

        theRecipient = aRecipient()
            .withId(recipientId)
            .withEmail("jane.doe@gmail.com")
            .build()
        theNewsletterConfiguration = aNewsletterConfiguration()
            .withRecipientId(recipientId)
            .withSources(aSource)
            .build()

        recipientPort.save(theRecipient)
        newsletterConfigurationPort.save(theNewsletterConfiguration)

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
        mockMvc.post("/api/v1/newsletter-configuration/${theNewsletterConfiguration.id}/newsletter") {
            bearerToken(authorizedToken)
        }

        // Then
        val newsletter = newsletterPort
            .ofNewsletterConfigurationId(theNewsletterConfiguration.id)
            .first()

        assertThat(newsletter.newsletterConfigurationId).isEqualTo(theNewsletterConfiguration.id)
        assertThat(newsletter.recipient)
            .usingRecursiveComparison()
            .isEqualTo(theRecipient)

        verify(mailer).sendMail(
            check {
                assertThat(it.recipients).hasSize(1)
                assertThat(it.recipients.first().address).isEqualTo(theRecipient.email)
            }
        )
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
