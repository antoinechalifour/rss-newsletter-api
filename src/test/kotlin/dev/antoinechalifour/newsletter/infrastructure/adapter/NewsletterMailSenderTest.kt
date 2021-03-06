package dev.antoinechalifour.newsletter.infrastructure.adapter

import com.nhaarman.mockitokotlin2.check
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import dev.antoinechalifour.newsletter.NewsletterTestBuilder.Companion.aNewsletter
import dev.antoinechalifour.newsletter.RecipientTestBuilder.Companion.aRecipient
import dev.antoinechalifour.newsletter.RecipientTestBuilder.Companion.aSender
import dev.antoinechalifour.newsletter.asTestResourceFileContent
import dev.antoinechalifour.newsletter.asserts.EmailAssert.Companion.assertThat
import dev.antoinechalifour.newsletter.infrastructure.http.mjml.MjmlService
import dev.antoinechalifour.newsletter.string
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.simplejavamail.api.mailer.Mailer
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect
import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

internal class NewsletterMailSenderTest {
    private lateinit var mjmlService: MjmlService
    private lateinit var mailer: Mailer
    private var mockWebServer = MockWebServer()

    @BeforeEach
    fun setup() {
        mockWebServer.start()
        mailer = mock()

        mjmlService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MjmlService::class.java)
    }

    @AfterEach
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `send the email to the recipient`() {
        // Given
        val theSender = aSender().build()
        val theRecipient = aRecipient().build()
        val newsletterSmtpAdapter =
            NewsletterMailSender(
                aTemplateEngine(),
                mailer,
                mjmlService
            )
        mockWebServer.enqueue(aMjmlResponse())

        // When
        newsletterSmtpAdapter.send(aNewsletter().build())

        // Then
        mockWebServer.takeRequest().apply {
            assertThat(method).isEqualTo("POST")
            assertThat(body.string())
                .isEqualTo("/test-http/mjml-request.json".asTestResourceFileContent())
        }

        verify(mailer).sendMail(
            check {
                assertThat(it).hasBeenSentBy(theSender)
                assertThat(it).hasOnlyRecipient(theRecipient)
                assertThat(it).hasSubject("Today's newsletter")
                assertThat(it).hasContent("some mjml compiled html")
            }
        )
    }

    private fun aTemplateEngine() = SpringTemplateEngine().apply {
        addDialect(Java8TimeDialect())
        setTemplateResolver(aTemplateResolver())
    }

    private fun aTemplateResolver() = ClassLoaderTemplateResolver().apply {
        prefix = "test-emails/"
        suffix = ".html"
        templateMode = TemplateMode.HTML
        characterEncoding = "UTF-8"
    }

    private fun aMjmlResponse() = MockResponse()
        .setResponseCode(HttpURLConnection.HTTP_OK)
        .setBody("/test-http/mjml-response.json".asTestResourceFileContent())
}
