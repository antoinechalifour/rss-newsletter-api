package dev.antoinechalifour.newsletter.application

import dev.antoinechalifour.newsletter.domain.Newsletter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class NewsletterResponse(
    id: UUID,
    newsletterConfigurationId: UUID,
    sentAt: LocalDateTime,
    val articles: List<ArticleResponse>
) {
    val id = id.toString()
    val newsletterConfigurationId = newsletterConfigurationId.toString()
    val sentAt = sentAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    companion object {
        fun of(newsletter: Newsletter) = NewsletterResponse(
            newsletter.id,
            newsletter.newsletterConfigurationId,
            newsletter.sentAt,
            ArticleResponse.ofAll(newsletter.articles)
        )
    }
}
