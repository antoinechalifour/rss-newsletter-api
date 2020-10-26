package dev.antoinechalifour.newsletter.usecase

import dev.antoinechalifour.newsletter.domain.ArticlePort
import dev.antoinechalifour.newsletter.domain.Newsletter
import dev.antoinechalifour.newsletter.domain.NewsletterConfigurationPort
import dev.antoinechalifour.newsletter.domain.NewsletterPort
import dev.antoinechalifour.newsletter.domain.NewsletterSender
import dev.antoinechalifour.newsletter.domain.Recipient
import org.springframework.stereotype.Component
import java.time.Clock
import java.util.UUID

@Component
class SendNewsletter(
    private val recipient: Recipient,
    private val clock: Clock,
    private val newsletterPort: NewsletterPort,
    private val newsletterConfigurationPort: NewsletterConfigurationPort,
    private val articlePort: ArticlePort,
    private val newsletterSender: NewsletterSender
) {
    operator fun invoke(newsletterConfigurationId: String): Newsletter {
        val newletterConfigurationId = UUID.fromString(newsletterConfigurationId)

        return newsletterConfigurationPort.ofId(newletterConfigurationId).run {
            val articles = sources.map(articlePort::ofSource).flatten()

            Newsletter.forToday(recipient, newletterConfigurationId, articles, clock)
        }.apply {
            if (isWorthSending()) {
                newsletterSender.send(this)
                newsletterPort.save(this)
            }
        }
    }
}
