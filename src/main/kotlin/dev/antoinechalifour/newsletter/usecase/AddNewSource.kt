package dev.antoinechalifour.newsletter.usecase

import dev.antoinechalifour.newsletter.domain.NewsletterConfiguration
import dev.antoinechalifour.newsletter.domain.NewsletterConfigurationPort
import dev.antoinechalifour.newsletter.domain.Source
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class AddNewSource(val newsletterConfigurationPort: NewsletterConfigurationPort) { // TODO: rename this use case
    // TODO: newsletterId -> newsletterConfigurationId
    operator fun invoke(newsletterId: String, url: String): NewsletterConfiguration {
        val newsletterConfiguration = newsletterConfigurationPort
            .ofId(UUID.fromString(newsletterId))
            .apply { newSource(url) }

        newsletterConfigurationPort.save(newsletterConfiguration)

        return newsletterConfiguration
    }
}
