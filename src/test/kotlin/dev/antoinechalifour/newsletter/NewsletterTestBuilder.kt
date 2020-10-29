package dev.antoinechalifour.newsletter

import dev.antoinechalifour.newsletter.RecipientTestBuilder.Companion.aRecipient
import dev.antoinechalifour.newsletter.domain.Article
import dev.antoinechalifour.newsletter.domain.Newsletter
import java.time.LocalDateTime
import java.util.UUID

class NewsletterTestBuilder {

    companion object {
        fun aNewsletter() = NewsletterTestBuilder()
    }

    private var id = UUID.randomUUID()
    private var newsletterConfigurationId = UUID.randomUUID()
    private var recipient = aRecipient().build()
    private var articles = emptyList<Article>()
    private var sentAt = LocalDateTime.of(2020, 11, 1, 12, 30)

    fun withArticles(vararg theArticles: Article) = apply { articles = theArticles.toList() }
    fun withNewsletterConfigurationId(theNewsletterConfigurationId: UUID) =
        apply { newsletterConfigurationId = theNewsletterConfigurationId }
    fun sentAt(date: LocalDateTime) = apply { sentAt = date }
    fun build() = Newsletter(id, newsletterConfigurationId, recipient, articles, sentAt)
}
