package dev.antoinechalifour.newsletter.application

import dev.antoinechalifour.newsletter.usecase.CreateNewsletterConfiguration
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/newsletter-configuration")
class NewsletterConfigurationController(val createNewsletterConfiguration: CreateNewsletterConfiguration) {

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun post(): ResponseEntity<Any> = createNewsletterConfiguration().run {
        ResponseEntity.status(201)
            .body(NewsletterConfigurationResponse.of(this))
    }

}
