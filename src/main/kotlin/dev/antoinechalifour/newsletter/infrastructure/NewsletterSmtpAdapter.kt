package dev.antoinechalifour.newsletter.infrastructure

import dev.antoinechalifour.newsletter.domain.Newsletter
import dev.antoinechalifour.newsletter.domain.NewsletterPort
import org.springframework.stereotype.Component

@Component
class NewsletterSmtpAdapter : NewsletterPort {
    override fun send(newsletter: Newsletter) {
        println("Sending newsletter:$newsletter")
    }
}