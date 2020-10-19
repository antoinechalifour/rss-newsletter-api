package dev.antoinechalifour.newsletter.usecase

import dev.antoinechalifour.newsletter.domain.*
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class SendNewsletter(
    private val recipient: Recipient,
    private val clock: Clock,
    private val sourcePort: SourcePort,
    private val articlePort: ArticlePort,
    private val newsletterPort: NewsletterPort
) {
    operator fun invoke() {
        val fromDate = yesterday()

        val sources = sourcePort.all()
        val articles = sources.map(articlePort::ofSource)
            .flatten()
            .filter { it.pubDate.isAfter(fromDate) }
        val newsletter = Newsletter(recipient, articles)

        newsletterPort.send(newsletter)
    }

    private fun yesterday() =
        LocalDateTime.now(clock).minusDays(1).withHour(12).withMinute(30)
}
