package dev.antoinechalifour.newsletter.domain

import java.util.UUID

interface NewsletterPort {
    fun all(): List<Newsletter>
    fun save(newsletter: Newsletter)
    fun ofId(id: UUID): Newsletter
    fun ofNewsletterConfigurationId(newsletterConfigurationId: UUID): List<Newsletter>
}
