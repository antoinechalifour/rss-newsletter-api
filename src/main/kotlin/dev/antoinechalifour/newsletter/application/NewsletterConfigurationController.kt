package dev.antoinechalifour.newsletter.application

import dev.antoinechalifour.newsletter.domain.NewsletterConfiguration
import dev.antoinechalifour.newsletter.usecase.CreateNewsletterConfiguration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/newsletter-configuration")
class NewsletterConfigurationController(val createNewsletterConfiguration: CreateNewsletterConfiguration) {

    @PostMapping
    fun post(): ResponseEntity<Any> {
        val newsletterConfiguration = createNewsletterConfiguration()

        return ResponseEntity.status(201).body(NewsletterConfigurationResponse.of(newsletterConfiguration))
    }

    class NewsletterConfigurationResponse(val id: String) {
        companion object {
            fun of(newsletterConfiguration: NewsletterConfiguration) = NewsletterConfigurationResponse(
                newsletterConfiguration.id.toString()
            )
        }
    }
}
