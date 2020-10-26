package dev.antoinechalifour.newsletter.domain

interface NewsletterSender {
    fun send(newsletter: Newsletter)
}
