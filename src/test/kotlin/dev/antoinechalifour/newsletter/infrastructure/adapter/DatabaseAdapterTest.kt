package dev.antoinechalifour.newsletter.infrastructure.adapter

import dev.antoinechalifour.newsletter.infrastructure.database.NewsletterConfigurationRepository
import dev.antoinechalifour.newsletter.infrastructure.database.NewsletterRepository
import org.springframework.beans.factory.annotation.Autowired

open class DatabaseAdapterTest {

    @Autowired
    private lateinit var newsletterRepository: NewsletterRepository

    @Autowired
    private lateinit var newsletterConfigurationRepository: NewsletterConfigurationRepository

    protected fun cleanDatabase() {
        newsletterConfigurationRepository.deleteAll()
        newsletterRepository.deleteAll()
    }
}
