package dev.antoinechalifour.newsletter.usecase

import com.nhaarman.mockitokotlin2.*
import dev.antoinechalifour.newsletter.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

internal class SendNewsletterTest {
    private lateinit var sourcePort: SourcePort
    private lateinit var newsletterPort: NewsletterPort
    private lateinit var articlePort: ArticlePort
    private val clock = Clock.fixed(
        now(),
        ZoneId.of("Europe/Paris")
    )


    @BeforeEach
    fun setup() {
        sourcePort = mock()
        newsletterPort = mock()
        articlePort = mock()
    }

    @Test
    fun `sends the newsletter with articles form multiple sources`() {
        // Given
        val sendNewsletter = SendNewsletter(aRecipient(), clock, sourcePort, articlePort, newsletterPort)
        val sources = listOf(aTechSource(), aNewsSource())

        // When
        whenever(sourcePort.all()).thenReturn(sources)
        whenever(articlePort.ofSource(aTechSource())).thenReturn(listOf(aTechArticle()))
        whenever(articlePort.ofSource(aNewsSource())).thenReturn(listOf(aNewsArticle()))
        sendNewsletter()

        // Then
        verify(newsletterPort).send(check {
            assertThat(it.recipient).isEqualTo(aRecipient())
            assertThat(it.articles).isEqualTo(listOf(aTechArticle(), aNewsArticle()))
        })
    }

    @Test
    fun `sends only articles published from yesteday 12 30pm`() {
        // Given
        val sendNewsletter = SendNewsletter(aRecipient(), clock, sourcePort, articlePort, newsletterPort)
        val sources = listOf(aTechSource(), aNewsSource())

        // When
        whenever(sourcePort.all()).thenReturn(sources)
        whenever(articlePort.ofSource(aTechSource())).thenReturn(
            listOf(
                aTechArticle(),
                aTechArticleFromYesterday()
            )
        )
        sendNewsletter()

        // Then
        verify(newsletterPort).send(check {
            assertThat(it.recipient).isEqualTo(aRecipient())
            assertThat(it.articles).isEqualTo(listOf(aTechArticle()))
        })
    }

    @Test
    fun `does not send the newsletter when no articles have been published`() {
        // Given
        val sendNewsletter = SendNewsletter(aRecipient(), clock, sourcePort, articlePort, newsletterPort)
        val sources = listOf(aTechSource(), aNewsSource())

        // When
        whenever(sourcePort.all()).thenReturn(sources)
        whenever(articlePort.ofSource(aTechSource())).thenReturn(emptyList())
        sendNewsletter()

        // Then
        verify(newsletterPort, never()).send(any())
    }

    private fun aRecipient() = Recipient("John Doe", "john.doe@email.com")

    private fun aNewsSource() = Source("https://www.lemonde.fr/rss/une.xml")

    private fun aTechSource() = Source("https://blog.octo.com/feed/")

    private fun aTechArticle() =
        Article("Some tech article title", "http://tech.com/link", today())

    private fun aTechArticleFromYesterday() =
        Article("Some older tech article title", "http://tech.com/link", yesterday())

    private fun aNewsArticle() =
        Article("Some news article title", "http://news.com/link", today())

    private fun now() = Instant.parse("2020-10-19T17:30:00.00Z")

    private fun today() = LocalDateTime.of(2020, 10, 19, 14, 30)

    private fun yesterday() = today().minusDays(1).withHour(12).withMinute(29)
}