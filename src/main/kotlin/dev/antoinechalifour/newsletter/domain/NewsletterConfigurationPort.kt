package dev.antoinechalifour.newsletter.domain

interface NewsletterConfigurationPort {
    fun save(newsletterConfiguration: NewsletterConfiguration)

}
