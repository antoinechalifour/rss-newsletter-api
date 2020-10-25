package dev.antoinechalifour.newsletter.usecase

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.check
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.antoinechalifour.newsletter.domain.Article
import dev.antoinechalifour.newsletter.domain.ArticlePort
import dev.antoinechalifour.newsletter.domain.NewsletterConfiguration
import dev.antoinechalifour.newsletter.domain.NewsletterConfigurationPort
import dev.antoinechalifour.newsletter.domain.NewsletterPort
import dev.antoinechalifour.newsletter.domain.Recipient
import dev.antoinechalifour.newsletter.domain.Source
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

internal class SendNewsletterTest {
    private lateinit var newsletterConfigurationPort: NewsletterConfigurationPort
    private lateinit var newsletterPort: NewsletterPort
    private lateinit var articlePort: ArticlePort
    private val clock = Clock.fixed(
        now(),
        ZoneId.of("Europe/Paris")
    )

    @BeforeEach
    fun setup() {
        newsletterConfigurationPort = mock()
        newsletterPort = mock()
        articlePort = mock()
    }

    @Test
    fun `sends the newsletter with articles from multiple sources`() {
        // Given
        val sendNewsletter =
            SendNewsletter(aRecipient(), clock, newsletterConfigurationPort, articlePort, newsletterPort)
        val newsletterConfiguration = aNewsletterConfiguration()

        whenever(newsletterConfigurationPort.ofId(newsletterConfiguration.id)).thenReturn(newsletterConfiguration)
        whenever(articlePort.ofSource(aTechSource())).thenReturn(listOf(aTechArticle()))
        whenever(articlePort.ofSource(aNewsSource())).thenReturn(listOf(aNewsArticle()))

        // When
        sendNewsletter(newsletterConfiguration.id.toString())

        // Then
        verify(newsletterPort).send(
            check {
                assertThat(it.recipient).isEqualTo(aRecipient())
                assertThat(it.articles).isEqualTo(listOf(aTechArticle(), aNewsArticle()))
            }
        )
    }

    @Test
    fun `sends only articles published from yesteday 12 30pm`() {
        // Given
        val sendNewsletter =
            SendNewsletter(aRecipient(), clock, newsletterConfigurationPort, articlePort, newsletterPort)
        val newsletterConfiguration = aNewsletterConfiguration()
        whenever(newsletterConfigurationPort.ofId(newsletterConfiguration.id)).thenReturn(newsletterConfiguration)
        whenever(articlePort.ofSource(aTechSource())).thenReturn(
            listOf(
                aTechArticle(),
                aTechArticleFromYesterday()
            )
        )

        // When
        sendNewsletter(newsletterConfiguration.id.toString())

        // Then
        verify(newsletterPort).send(
            check {
                assertThat(it.recipient).isEqualTo(aRecipient())
                assertThat(it.articles).isEqualTo(listOf(aTechArticle()))
            }
        )
    }

    @Test
    fun `does not send the newsletter when no articles have been published`() {
        // Given
        val sendNewsletter =
            SendNewsletter(aRecipient(), clock, newsletterConfigurationPort, articlePort, newsletterPort)
        val newsletterConfiguration = aNewsletterConfiguration()
        whenever(newsletterConfigurationPort.ofId(newsletterConfiguration.id)).thenReturn(newsletterConfiguration)
        whenever(articlePort.ofSource(aTechSource())).thenReturn(emptyList())

        // When
        sendNewsletter(newsletterConfiguration.id.toString())

        // Then
        verify(newsletterPort, never()).send(any())
    }

    private fun aRecipient() = Recipient("John Doe", "john.doe@email.com")

    private fun aNewsSource() = Source(
        UUID.fromString("67e59d0e-c7ab-4027-a8d5-74adb66eefc2"),
        "https://www.lemonde.fr/rss/une.xml"
    )

    private fun aTechSource() = Source(
        UUID.fromString("a417d43b-4441-48b0-95de-13cbc3050745"),
        "https://blog.octo.com/feed/"
    )

    private fun aTechArticle() =
        Article("Some tech article title", "http://tech.com/link", today())

    private fun aTechArticleFromYesterday() =
        Article("Some older tech article title", "http://tech.com/link", yesterday())

    private fun aNewsArticle() =
        Article("Some news article title", "http://news.com/link", today())

    private fun aNewsletterConfiguration() =
        NewsletterConfiguration(UUID.randomUUID(), mutableListOf(aTechSource(), aNewsSource()))

    private fun now() = Instant.parse("2020-10-19T17:30:00.00Z")

    private fun today() = LocalDateTime.of(2020, 10, 19, 14, 30)

    private fun yesterday() = today().minusDays(1).withHour(12).withMinute(29)
}
