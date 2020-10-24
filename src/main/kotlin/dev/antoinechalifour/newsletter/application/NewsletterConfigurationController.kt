package dev.antoinechalifour.newsletter.application

import dev.antoinechalifour.newsletter.infrastructure.database.NewsletterConfigurationDatabase
import dev.antoinechalifour.newsletter.infrastructure.database.NewsletterConfigurationRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/newsletter-configuration")
class NewsletterConfigurationController(val newsletterConfigurationRepository: NewsletterConfigurationRepository) {

    @PostMapping
    fun post() {
        newsletterConfigurationRepository.save(NewsletterConfigurationDatabase(UUID.randomUUID()))
    }
}