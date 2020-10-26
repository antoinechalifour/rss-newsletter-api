package dev.antoinechalifour.newsletter.domain

interface NewsletterPort {
    fun all(): List<Newsletter>
    fun save(newsletter: Newsletter)

}
