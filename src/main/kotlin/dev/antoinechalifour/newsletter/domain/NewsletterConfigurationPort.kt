package dev.antoinechalifour.newsletter.domain

import java.util.UUID

interface NewsletterConfigurationPort {
    fun ofId(id: UUID): NewsletterConfiguration
    fun save(newsletterConfiguration: NewsletterConfiguration)
}
