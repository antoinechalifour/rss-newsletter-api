package dev.antoinechalifour.newsletter.application

import dev.antoinechalifour.newsletter.usecase.CreateNewsletterConfiguration
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/newsletter-configuration")
class NewsletterConfigurationController(val createNewsletterConfiguration: CreateNewsletterConfiguration) {

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun post(): ResponseEntity<NewsletterConfigurationResponse> = createNewsletterConfiguration(HARDCODED_USER_ID).run {
        ResponseEntity.status(201)
            .body(NewsletterConfigurationResponse.of(this))
    }

    companion object {
        val HARDCODED_USER_ID = UUID.fromString("7b82df23-71b5-49bc-a4eb-ca42480c194d")
    }
}
