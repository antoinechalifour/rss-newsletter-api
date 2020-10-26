package dev.antoinechalifour.newsletter.application

import dev.antoinechalifour.newsletter.domain.Newsletter

class NewsletterResponse(
    val id: String,
    val newsletterConfigurationId: String,
    val articles: List<ArticleResponse>
) {
    companion object {
        fun of(newsletter: Newsletter) = NewsletterResponse(
            newsletter.id.toString(),
            newsletter.newsletterConfigurationId.toString(),
            ArticleResponse.ofAll(newsletter.articles)
        )
    }
}
