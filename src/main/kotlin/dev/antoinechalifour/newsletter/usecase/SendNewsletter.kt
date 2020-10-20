package dev.antoinechalifour.newsletter.usecase

import dev.antoinechalifour.newsletter.domain.ArticlePort
import dev.antoinechalifour.newsletter.domain.Newsletter
import dev.antoinechalifour.newsletter.domain.NewsletterPort
import dev.antoinechalifour.newsletter.domain.Recipient
import dev.antoinechalifour.newsletter.domain.SourcePort
import org.springframework.stereotype.Component
import java.time.Clock

@Component
class SendNewsletter(
    private val recipient: Recipient,
    private val clock: Clock,
    private val sourcePort: SourcePort,
    private val articlePort: ArticlePort,
    private val newsletterPort: NewsletterPort
) {
    operator fun invoke() {
        val sources = sourcePort.all()
        val articles = sources.map(articlePort::ofSource).flatten()

        Newsletter.forToday(recipient, articles, clock).run {
            if (isWorthSending()) newsletterPort.send(this)
        }
    }
}
