package dev.antoinechalifour.newsletter.usecase

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.check
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.antoinechalifour.newsletter.ArticleTestBuilder
import dev.antoinechalifour.newsletter.NewsletterConfigurationTestBuilder
import dev.antoinechalifour.newsletter.RecipientTestBuilder
import dev.antoinechalifour.newsletter.SourceTestBuilder
import dev.antoinechalifour.newsletter.asserts.NewsletterAssert.Companion.assertThat
import dev.antoinechalifour.newsletter.domain.ArticlePort
import dev.antoinechalifour.newsletter.domain.NewsletterConfigurationPort
import dev.antoinechalifour.newsletter.domain.NewsletterPort
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

internal class SendNewsletterTest {
    private lateinit var newsletterConfigurationPort: NewsletterConfigurationPort
    private lateinit var newsletterPort: NewsletterPort
    private lateinit var articlePort: ArticlePort
    private val clock = Clock.fixed(now(), ZoneId.of("Europe/Paris"))

    @BeforeEach
    fun setup() {
        newsletterConfigurationPort = mock()
        newsletterPort = mock()
        articlePort = mock()
    }

    @Test
    fun `sends the newsletter with articles from multiple sources`() {
        // Given
        val theRecipient = RecipientTestBuilder().build()
        val aTechSource = SourceTestBuilder().withUrl("https://blog.octo.com/feed/").build()
        val aNewsSource = SourceTestBuilder().withUrl("https://www.lemonde.fr/rss/une.xml").build()
        val aTechArticle = ArticleTestBuilder(clock).withUrl("https://blog.octo.com/article-1").build()
        val aNewsArticles = ArticleTestBuilder(clock).withUrl("https://www.lemonde.fr/article-2").build()
        val theNewsletterConfiguration = NewsletterConfigurationTestBuilder()
            .withSources(aTechSource, aNewsSource)
            .build()

        val sendNewsletter = SendNewsletter(
            theRecipient, clock, newsletterConfigurationPort, articlePort, newsletterPort
        )

        whenever(newsletterConfigurationPort.ofId(theNewsletterConfiguration.id)).thenReturn(theNewsletterConfiguration)
        whenever(articlePort.ofSource(aTechSource)).thenReturn(listOf(aTechArticle))
        whenever(articlePort.ofSource(aNewsSource)).thenReturn(listOf(aNewsArticles))

        // When
        sendNewsletter(theNewsletterConfiguration.id.toString())

        // Then
        verify(newsletterPort).send(
            check {
                assertThat(it).isSentTo(theRecipient)
                assertThat(it).hasOnlyTheArticles(aTechArticle, aNewsArticles)
            }
        )
    }

    @Test
    fun `sends only articles published from yesterday after 12 30pm`() {
        // Given
        val aSource = SourceTestBuilder().build()
        val anArticleFromToday = ArticleTestBuilder(clock).build()
        val anArticleFromYesterday = ArticleTestBuilder(clock).fromYesterdayBefore1230().build()
        val theNewsletterConfiguration = NewsletterConfigurationTestBuilder()
            .withSources(aSource)
            .build()

        val sendNewsletter = SendNewsletter(
            RecipientTestBuilder().build(),
            clock,
            newsletterConfigurationPort,
            articlePort,
            newsletterPort
        )

        whenever(newsletterConfigurationPort.ofId(theNewsletterConfiguration.id)).thenReturn(theNewsletterConfiguration)
        whenever(articlePort.ofSource(aSource)).thenReturn(listOf(anArticleFromToday, anArticleFromYesterday))

        // When
        sendNewsletter(theNewsletterConfiguration.id.toString())

        // Then
        verify(newsletterPort).send(
            check { assertThat(it).hasOnlyTheArticles(anArticleFromToday) }
        )
    }

    @Test
    fun `does not send the newsletter when no articles have been published`() {
        // Given
        val aSource = SourceTestBuilder().build()
        val theNewsletterConfiguration = NewsletterConfigurationTestBuilder()
            .withSources(aSource)
            .build()

        val sendNewsletter = SendNewsletter(
            RecipientTestBuilder().build(),
            clock,
            newsletterConfigurationPort,
            articlePort,
            newsletterPort
        )

        whenever(newsletterConfigurationPort.ofId(theNewsletterConfiguration.id)).thenReturn(theNewsletterConfiguration)
        whenever(articlePort.ofSource(aSource)).thenReturn(emptyList())

        // When
        sendNewsletter(theNewsletterConfiguration.id.toString())

        // Then
        verify(newsletterPort, never()).send(any())
    }

    private fun now() = Instant.parse("2020-10-19T17:30:00.00Z")
}
