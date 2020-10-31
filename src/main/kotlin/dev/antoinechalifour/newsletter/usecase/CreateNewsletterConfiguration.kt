package dev.antoinechalifour.newsletter.usecase

import dev.antoinechalifour.newsletter.domain.NewsletterConfiguration
import dev.antoinechalifour.newsletter.domain.NewsletterConfigurationPort
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CreateNewsletterConfiguration(val newsletterConfigurationPort: NewsletterConfigurationPort) {
    operator fun invoke(recipientId: UUID) = NewsletterConfiguration(UUID.randomUUID(), recipientId)
        .apply { newsletterConfigurationPort.save(this) }
}
