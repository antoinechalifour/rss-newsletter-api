package dev.antoinechalifour.newsletter.application

import com.nhaarman.mockitokotlin2.whenever
import dev.antoinechalifour.newsletter.ArticleTestBuilder.Companion.anArticle
import dev.antoinechalifour.newsletter.NewsletterTestBuilder.Companion.aNewsletter
import dev.antoinechalifour.newsletter.basicAuth
import dev.antoinechalifour.newsletter.usecase.SendNewsletter
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.post
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
internal class NewsletterControllerPostTest : ApiIntegrationTest() {

    private val clock = Clock.fixed(now(), ZoneId.of("Europe/Paris"))

    @MockBean
    private lateinit var sendNewsletter: SendNewsletter

    @Test
    fun `should not be accessible without authentication`() {
        checkAuthentication { post("/api/v1/newsletter-configuration/configuration-id/newsletter") }
    }

    @Test
    fun `should send the newsletter`() {
        val newsletterConfigurationId = UUID.randomUUID()
        val anArticle = anArticle(clock)
            .withTitle("The article title")
            .withUrl("https://blog.octo.com/article")
            .withSource("Blog Octo")
            .build()
        val newsletter = aNewsletter()
            .withNewsletterConfigurationId(newsletterConfigurationId)
            .withArticles(anArticle)
            .build()
        whenever(sendNewsletter.invoke(newsletterConfigurationId.toString())).thenReturn(newsletter)

        mockMvc.post("/api/v1/newsletter-configuration/$newsletterConfigurationId/newsletter") {
            basicAuth("admin", "passwd")
        }.andExpect {
            status { isOk }
            jsonPath("$.id", equalTo(newsletter.id.toString()))
            jsonPath("$.newsletterConfigurationId", equalTo(newsletter.newsletterConfigurationId.toString()))
            jsonPath("$.articles.length()", equalTo(1))
            jsonPath("$.articles[0].title", equalTo("The article title"))
            jsonPath("$.articles[0].url", equalTo("https://blog.octo.com/article"))
            jsonPath("$.articles[0].pubDate", equalTo("2020-10-19T19:30:00"))
            jsonPath("$.articles[0].source", equalTo("Blog Octo"))
        }
    }

    private fun now() = Instant.parse("2020-10-19T17:30:00.00Z")
}
