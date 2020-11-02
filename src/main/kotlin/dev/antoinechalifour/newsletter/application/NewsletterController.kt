package dev.antoinechalifour.newsletter.application

import dev.antoinechalifour.newsletter.usecase.SendNewsletter
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/newsletter-configuration/{newsletterConfigurationId}")
class NewsletterController(
    val authenticationService: AuthenticationService,
    val sendNewsletter: SendNewsletter
) {

    @PostMapping("/newsletter")
    fun post(@PathVariable newsletterConfigurationId: String): NewsletterResponse =
        sendNewsletter(newsletterConfigurationId).run { NewsletterResponse.of(this) }
}
