package dev.antoinechalifour.newsletter.usecase

import com.nhaarman.mockitokotlin2.check
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.antoinechalifour.newsletter.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class SendNewsletterTest {
    private lateinit var sourcePort: SourcePort
    private lateinit var newsletterPort: NewsletterPort
    private lateinit var articlePort: ArticlePort

    @BeforeEach
    fun setup() {
        sourcePort = mock()
        newsletterPort = mock()
        articlePort = mock()
    }

    @Test
    fun `sends the newsletter with articles form multiple sources`() {
        // Given
        val sendNewsletter = SendNewsletter(aRecipient(), sourcePort, articlePort, newsletterPort)
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

    private fun aRecipient() = Recipient("John Doe", "john.doe@email.com")

    private fun aNewsSource() = Source("https://www.lemonde.fr/rss/une.xml")

    private fun aTechSource() = Source("https://blog.octo.com/feed/")

    private fun aTechArticle() =
        Article("Some tech article title", "http://tech.com/link", today())

    private fun aNewsArticle() =
        Article("Some news article title", "http://news.com/link", today())

    private fun today() = LocalDateTime.of(2020, 10, 19, 14, 30)
}