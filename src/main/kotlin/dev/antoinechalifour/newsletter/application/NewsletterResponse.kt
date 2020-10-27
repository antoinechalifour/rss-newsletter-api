package dev.antoinechalifour.newsletter.application

import dev.antoinechalifour.newsletter.domain.Newsletter
import java.util.UUID

class NewsletterResponse(
    id: UUID,
    newsletterConfigurationId: UUID,
    val articles: List<ArticleResponse>
) {
    val id = id.toString()
    val newsletterConfigurationId = newsletterConfigurationId.toString()

    companion object {
        fun of(newsletter: Newsletter) = NewsletterResponse(
            newsletter.id,
            newsletter.newsletterConfigurationId,
            ArticleResponse.ofAll(newsletter.articles)
        )
    }
}
