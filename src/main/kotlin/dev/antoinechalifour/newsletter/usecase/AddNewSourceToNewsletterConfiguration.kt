package dev.antoinechalifour.newsletter.usecase

import dev.antoinechalifour.newsletter.domain.NewsletterConfigurationPort
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class AddNewSourceToNewsletterConfiguration(val newsletterConfigurationPort: NewsletterConfigurationPort) {
    operator fun invoke(newsletterConfigurationId: String, url: String, name: String = "") =
        newsletterConfigurationPort
            .ofId(UUID.fromString(newsletterConfigurationId))
            .apply {
                newSource(url, name)
                newsletterConfigurationPort.save(this)
            }
}
