package dev.antoinechalifour.newsletter.infrastructure.adapter

import dev.antoinechalifour.newsletter.domain.Newsletter
import dev.antoinechalifour.newsletter.domain.NewsletterPort
import dev.antoinechalifour.newsletter.infrastructure.database.NewsletterDatabase
import dev.antoinechalifour.newsletter.infrastructure.database.NewsletterRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class NewsletterDatabaseAdapter(val newsletterRepository: NewsletterRepository) : NewsletterPort {
    override fun all() = newsletterRepository.findAll().map { it.toNewsletter() }

    override fun save(newsletter: Newsletter) {
        newsletterRepository.save(NewsletterDatabase.of(newsletter))
    }

    override fun ofId(id: UUID): Newsletter = newsletterRepository
        .findById(id)
        .map { it.toNewsletter() }
        .orElseThrow()
}
