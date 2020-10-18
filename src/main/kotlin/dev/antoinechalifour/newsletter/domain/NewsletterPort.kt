package dev.antoinechalifour.newsletter.domain

interface NewsletterPort {
    fun send(newsletter: Newsletter)
}
