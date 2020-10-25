package dev.antoinechalifour.newsletter.acceptance

import dev.antoinechalifour.newsletter.infrastructure.database.NewsletterConfigurationRepository
import org.springframework.beans.factory.annotation.Autowired

abstract class AcceptanceTest {

    @Autowired
    protected lateinit var newsletterConfigurationRepository: NewsletterConfigurationRepository

    protected fun cleanupDatabase() {
        newsletterConfigurationRepository.deleteAll()
    }
}
