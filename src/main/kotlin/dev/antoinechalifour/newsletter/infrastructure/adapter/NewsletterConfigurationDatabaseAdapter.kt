package dev.antoinechalifour.newsletter.infrastructure.adapter

import dev.antoinechalifour.newsletter.domain.NewsletterConfiguration
import dev.antoinechalifour.newsletter.domain.NewsletterConfigurationPort
import dev.antoinechalifour.newsletter.infrastructure.database.NewsletterConfigurationDatabase
import dev.antoinechalifour.newsletter.infrastructure.database.NewsletterConfigurationRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class NewsletterConfigurationDatabaseAdapter(val newsletterConfigurationRepository: NewsletterConfigurationRepository) :
    NewsletterConfigurationPort {

    override fun ofId(id: UUID): NewsletterConfiguration = newsletterConfigurationRepository.findById(id)
        .map { it.toNewsletterConfiguration() }
        .orElseThrow { NoSuchElementException("Newsletter configuration $id was not found") }

    override fun all(): List<NewsletterConfiguration> = newsletterConfigurationRepository.findAll()
        .map { it.toNewsletterConfiguration() }

    override fun save(newsletterConfiguration: NewsletterConfiguration) {
        newsletterConfigurationRepository.save(NewsletterConfigurationDatabase.of(newsletterConfiguration))
    }
}
