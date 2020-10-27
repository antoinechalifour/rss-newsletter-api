package dev.antoinechalifour.newsletter.application

import dev.antoinechalifour.newsletter.domain.Source
import java.util.UUID

class SourceResponse(id: UUID, val url: String) {
    val id = id.toString()

    companion object {
        fun of(source: Source) = SourceResponse(source.id, source.url)
        fun ofAll(sources: MutableList<Source>) = sources.map { of(it) }
    }
}
