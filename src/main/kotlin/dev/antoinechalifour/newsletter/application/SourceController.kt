package dev.antoinechalifour.newsletter.application

import com.fasterxml.jackson.annotation.JsonProperty
import dev.antoinechalifour.newsletter.usecase.AddNewSource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/newsletter-configuration/{newsletterConfigurationId}/sources")
class SourceController(val addNewSource: AddNewSource) {

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun post(
        @PathVariable newsletterConfigurationId: String,
        @RequestBody body: AddNewSourceBody
    ): ResponseEntity<NewsletterConfigurationResponse> {
        val newsletterConfiguration = addNewSource(newsletterConfigurationId, body.url)

        return ResponseEntity.status(201)
            .body(NewsletterConfigurationResponse.of(newsletterConfiguration))
    }

    data class AddNewSourceBody(@JsonProperty("url") val url: String)

}
