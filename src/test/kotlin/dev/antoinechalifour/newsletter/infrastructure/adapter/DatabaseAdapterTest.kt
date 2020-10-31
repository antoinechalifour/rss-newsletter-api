package dev.antoinechalifour.newsletter.infrastructure.adapter

import dev.antoinechalifour.newsletter.infrastructure.database.NewsletterConfigurationRepository
import dev.antoinechalifour.newsletter.infrastructure.database.NewsletterRepository
import dev.antoinechalifour.newsletter.infrastructure.database.RecipientRepository
import org.springframework.beans.factory.annotation.Autowired

open class DatabaseAdapterTest {

    @Autowired
    protected lateinit var recipientRepository: RecipientRepository

    @Autowired
    protected lateinit var newsletterRepository: NewsletterRepository

    @Autowired
    protected lateinit var newsletterConfigurationRepository: NewsletterConfigurationRepository

    protected fun cleanDatabase() {
        newsletterConfigurationRepository.deleteAll()
        recipientRepository.deleteAll()
        newsletterRepository.deleteAll()
    }
}
