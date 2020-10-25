package dev.antoinechalifour.newsletter.application

import dev.antoinechalifour.newsletter.domain.NewsletterConfiguration
import java.util.UUID

class NewsletterConfigurationResponse(
    uuid: UUID,
    val sources: List<SourceResponse>
) {
    var id: String = uuid.toString()

    companion object {
        fun of(newsletterConfiguration: NewsletterConfiguration) =
            NewsletterConfigurationResponse(
                newsletterConfiguration.id,
                SourceResponse.ofAll(newsletterConfiguration.sources)
            )
    }
}