package dev.antoinechalifour.newsletter.domain

interface NewsletterPort {
    fun save(newsletter: Newsletter)

}
