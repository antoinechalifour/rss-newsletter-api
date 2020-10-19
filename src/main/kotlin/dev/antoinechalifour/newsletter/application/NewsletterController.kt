package dev.antoinechalifour.newsletter.application

import dev.antoinechalifour.newsletter.usecase.SendNewsletter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/newsletter")
class NewsletterController(val sendNewsletter: SendNewsletter) {
    @GetMapping
    fun get(): String {
        sendNewsletter()

        return "Sent"
    }
}
