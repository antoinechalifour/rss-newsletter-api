package dev.antoinechalifour.newsletter.usecase

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.check
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.antoinechalifour.newsletter.ArticleTestBuilder.Companion.anArticle
import dev.antoinechalifour.newsletter.NewsletterConfigurationTestBuilder.Companion.aNewsletterConfiguration
import dev.antoinechalifour.newsletter.RecipientTestBuilder.Companion.aRecipient
import dev.antoinechalifour.newsletter.SourceTestBuilder.Companion.aSource
import dev.antoinechalifour.newsletter.asserts.NewsletterAssert.Companion.assertThat
import dev.antoinechalifour.newsletter.domain.ArticlePort
import dev.antoinechalifour.newsletter.domain.NewsletterConfigurationPort
import dev.antoinechalifour.newsletter.domain.NewsletterPort
import dev.antoinechalifour.newsletter.domain.NewsletterSender
import dev.antoinechalifour.newsletter.domain.RecipientPort
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

internal class SendNewsletterTest {
    private lateinit var recipientPort: RecipientPort
    private lateinit var newsletterPort: NewsletterPort
    private lateinit var newsletterConfigurationPort: NewsletterConfigurationPort
    private lateinit var newsletterSender: NewsletterSender
    private lateinit var articlePort: ArticlePort
    private val clock = Clock.fixed(now(), ZoneId.of("Europe/Paris"))

    @BeforeEach
    fun setup() {
        recipientPort = mock()
        newsletterPort = mock()
        newsletterConfigurationPort = mock()
        newsletterSender = mock()
        articlePort = mock()
    }

    @Test
    fun `sends the newsletter with articles from multiple sources`() {
        // Given
        val theRecipient = aRecipient().withId(UUID.randomUUID()).build()
        val aTechSource = aSource().withUrl("https://blog.octo.com/feed/").build()
        val aNewsSource = aSource().withUrl("https://www.lemonde.fr/rss/une.xml").build()
        val aTechArticle = anArticle(clock).withUrl("https://blog.octo.com/article-1").build()
        val aNewsArticles = anArticle(clock).withUrl("https://www.lemonde.fr/article-2").build()
        val theNewsletterConfiguration = aNewsletterConfiguration()
            .withSources(aTechSource, aNewsSource)
            .withRecipientId(theRecipient.id)
            .build()

        val sendNewsletter = SendNewsletter(
            recipientPort, clock, newsletterPort, newsletterConfigurationPort, articlePort, newsletterSender
        )

        whenever(newsletterConfigurationPort.ofId(theNewsletterConfiguration.id)).thenReturn(theNewsletterConfiguration)
        whenever(recipientPort.ofId(theNewsletterConfiguration.recipientId)).thenReturn(theRecipient)
        whenever(articlePort.ofSource(aTechSource)).thenReturn(listOf(aTechArticle))
        whenever(articlePort.ofSource(aNewsSource)).thenReturn(listOf(aNewsArticles))

        // When
        sendNewsletter(theNewsletterConfiguration.id.toString())

        // Then
        verify(newsletterSender).send(
            check {
                assertThat(it).isSentTo(theRecipient)
                assertThat(it).hasOnlyTheArticles(aTechArticle, aNewsArticles)
            }
        )
    }

    @Test
    fun `sends only articles published from yesterday after 12 30pm`() {
        // Given
        val aSource = aSource().build()
        val anArticleFromToday = anArticle(clock).build()
        val anArticleFromYesterday = anArticle(clock).fromYesterdayBefore1230().build()
        val theNewsletterConfiguration = aNewsletterConfiguration()
            .withSources(aSource)
            .build()

        val sendNewsletter = SendNewsletter(
            recipientPort,
            clock,
            newsletterPort,
            newsletterConfigurationPort,
            articlePort,
            newsletterSender
        )

        whenever(newsletterConfigurationPort.ofId(theNewsletterConfiguration.id)).thenReturn(theNewsletterConfiguration)
        whenever(recipientPort.ofId(theNewsletterConfiguration.recipientId)).thenReturn(aRecipient().build())
        whenever(articlePort.ofSource(aSource)).thenReturn(listOf(anArticleFromToday, anArticleFromYesterday))

        // When
        sendNewsletter(theNewsletterConfiguration.id.toString())

        // Then
        verify(newsletterSender).send(
            check { assertThat(it).hasOnlyTheArticles(anArticleFromToday) }
        )
    }

    @Test
    fun `saves the newsletter if sent`() {
        // Given
        val theRecipient = aRecipient().build()
        val aTechSource = aSource().withUrl("https://blog.octo.com/feed/").build()
        val aTechArticle = anArticle(clock).withUrl("https://blog.octo.com/article-1").build()
        val theNewsletterConfiguration = aNewsletterConfiguration().withSources(aTechSource).build()

        val sendNewsletter = SendNewsletter(
            recipientPort, clock, newsletterPort, newsletterConfigurationPort, articlePort, newsletterSender
        )

        whenever(newsletterConfigurationPort.ofId(theNewsletterConfiguration.id)).thenReturn(theNewsletterConfiguration)
        whenever(recipientPort.ofId(theNewsletterConfiguration.recipientId)).thenReturn(theRecipient)
        whenever(articlePort.ofSource(aTechSource)).thenReturn(listOf(aTechArticle))

        // When
        val newsletter = sendNewsletter(theNewsletterConfiguration.id.toString())

        // Then
        assertThat(newsletter).hasBeenSentAt(LocalDateTime.now(clock))
        assertThat(newsletter).hasNewsletterConfigurationId(theNewsletterConfiguration.id)
        assertThat(newsletter).hasRecipient(theRecipient)
        assertThat(newsletter).hasOnlyTheArticles(aTechArticle)

        verify(newsletterPort).save(newsletter)
    }

    @Test
    fun `does not send the newsletter when no articles have been published`() {
        // Given
        val aSource = aSource().build()
        val theNewsletterConfiguration = aNewsletterConfiguration()
            .withSources(aSource)
            .build()

        val sendNewsletter = SendNewsletter(
            recipientPort,
            clock,
            newsletterPort,
            newsletterConfigurationPort,
            articlePort,
            newsletterSender
        )

        whenever(newsletterConfigurationPort.ofId(theNewsletterConfiguration.id)).thenReturn(theNewsletterConfiguration)
        whenever(recipientPort.ofId(theNewsletterConfiguration.recipientId)).thenReturn(aRecipient().build())
        whenever(articlePort.ofSource(aSource)).thenReturn(emptyList())

        // When
        sendNewsletter(theNewsletterConfiguration.id.toString())

        // Then
        verify(newsletterSender, never()).send(any())
    }

    private fun now() = Instant.parse("2020-10-19T17:30:00.00Z")
}
