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
class NewsletterConfigurationController(
    val createNewsletterConfiguration: CreateNewsletterConfiguration,
    val authenticationService: AuthenticationService
) {

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun post(): ResponseEntity<NewsletterConfigurationResponse> {
        val recipient = authenticationService.currentUser()
        val newsletterConfiguration = createNewsletterConfiguration(recipient.id)

        return ResponseEntity.status(201)
            .body(NewsletterConfigurationResponse.of(newsletterConfiguration))
    }

    companion object {
        val HARDCODED_USER_ID = UUID.fromString("7b82df23-71b5-49bc-a4eb-ca42480c194d")
    }
}
