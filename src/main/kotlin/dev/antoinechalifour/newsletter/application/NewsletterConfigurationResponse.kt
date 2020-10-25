package dev.antoinechalifour.newsletter.application

import dev.antoinechalifour.newsletter.domain.NewsletterConfiguration

class NewsletterConfigurationResponse(
    val id: String,
    val sources: List<SourceResponse>
) {
    companion object {
        fun of(newsletterConfiguration: NewsletterConfiguration) =
            NewsletterConfigurationResponse(
                newsletterConfiguration.id.toString(),
                newsletterConfiguration.sources.map { SourceResponse.of(it) }
            )
    }
}