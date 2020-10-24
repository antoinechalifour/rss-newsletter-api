package dev.antoinechalifour.newsletter.usecase

import dev.antoinechalifour.newsletter.domain.NewsletterConfiguration
import dev.antoinechalifour.newsletter.domain.NewsletterConfigurationPort
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CreateNewsletterConfiguration(val newsletterConfigurationPort: NewsletterConfigurationPort) {
    operator fun invoke(): NewsletterConfiguration {
        val newsletterConfiguration = NewsletterConfiguration(UUID.randomUUID())

        newsletterConfigurationPort.save(newsletterConfiguration)

        return newsletterConfiguration
    }
}
