package dev.antoinechalifour.newsletter.usecase

import com.nhaarman.mockitokotlin2.check
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.antoinechalifour.newsletter.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
        val sendNewsletter = SendNewsletter(sourcePort, articlePort, newsletterPort)
        val sources = listOf(aTechSource(), aNewsSource())

        // When
        whenever(sourcePort.all()).thenReturn(sources)
        whenever(articlePort.ofSource(aTechSource())).thenReturn(listOf(aTechArticle()))
        whenever(articlePort.ofSource(aNewsSource())).thenReturn(listOf(aNewsArticle()))
        sendNewsletter()

        // Then
        verify(newsletterPort).send(check {
            assertThat(it.articles).isEqualTo(listOf(aTechArticle(), aNewsArticle()))
        })
    }

    private fun aNewsSource() = Source("https://www.lemonde.fr/rss/une.xml")

    private fun aTechSource() = Source("https://blog.octo.com/feed/")

    private fun aTechArticle() =
        Article("Some tech article title", "http://tech.com/link", "Thu, 15 Oct 2020 08:39:02 +0000")

    private fun aNewsArticle() =
        Article("Some news article title", "http://news.com/link", "Thu, 15 Oct 2020 09/30:00 +0000")
}