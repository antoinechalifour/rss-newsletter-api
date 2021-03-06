package dev.antoinechalifour.newsletter.application

import dev.antoinechalifour.newsletter.domain.NewsletterConfiguration
import java.util.UUID

class NewsletterConfigurationResponse(
    id: UUID,
    val sources: List<SourceResponse>
) {
    val id: String = id.toString()

    companion object {
        fun of(newsletterConfiguration: NewsletterConfiguration) =
            NewsletterConfigurationResponse(
                newsletterConfiguration.id,
                SourceResponse.ofAll(newsletterConfiguration.sources)
            )
    }
}
