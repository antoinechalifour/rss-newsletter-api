package dev.antoinechalifour.newsletter.application

import dev.antoinechalifour.newsletter.usecase.SendNewsletter
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.time.ExperimentalTime

@RestController
@RequestMapping("/newsletter")
class NewsletterController(val sendNewsletter: SendNewsletter) {
    @ExperimentalTime
    @PostMapping
    fun post(): String {
        sendNewsletter()

        return "Sent"
    }
}
