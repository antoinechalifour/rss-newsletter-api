package dev.antoinechalifour.newsletter.usecase

import dev.antoinechalifour.newsletter.domain.ArticlePort
import dev.antoinechalifour.newsletter.domain.Newsletter
import dev.antoinechalifour.newsletter.domain.NewsletterPort
import dev.antoinechalifour.newsletter.domain.Recipient
import dev.antoinechalifour.newsletter.domain.SourcePort
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import java.time.Clock
import kotlin.time.ExperimentalTime

@Component
class SendNewsletter(
    private val recipient: Recipient,
    private val clock: Clock,
    private val sourcePort: SourcePort,
    private val articlePort: ArticlePort,
    private val newsletterPort: NewsletterPort
) {
    operator fun invoke() = runBlocking {
        val sources = sourcePort.all()
        val jobs = sources.map {
            GlobalScope.async {
                articlePort.ofSource(it)
            }
        }

        val articles = jobs.map { it.await() }.flatten()

        Newsletter.forToday(recipient, articles, clock).run {
            if (isWorthSending()) newsletterPort.send(this)
        }

    }
}
