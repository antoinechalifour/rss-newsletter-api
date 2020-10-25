package dev.antoinechalifour.newsletter.application

import dev.antoinechalifour.newsletter.domain.Source

class SourceResponse(val id: String, val url: String) {
    companion object {
        fun of(source: Source) = SourceResponse(source.id.toString(), source.url)
    }
}
