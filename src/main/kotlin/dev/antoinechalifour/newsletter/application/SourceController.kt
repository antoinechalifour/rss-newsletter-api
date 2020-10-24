package dev.antoinechalifour.newsletter.application

import com.fasterxml.jackson.annotation.JsonProperty
import dev.antoinechalifour.newsletter.domain.Source
import dev.antoinechalifour.newsletter.usecase.AddNewSource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/sources")
class SourceController(val addNewSource: AddNewSource) {

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun post(@RequestBody body: AddNewSourceBody): ResponseEntity<SourceResponse> {
        val source = addNewSource(body.url)

        return ResponseEntity.status(201)
            .body(SourceResponse.of(source))
    }

    data class AddNewSourceBody(@JsonProperty("url") val url: String)

    class SourceResponse(val id: String, val url: String) {
        companion object {
            fun of(source: Source) = SourceResponse(source.id.toString(), source.url)
        }
    }
}
