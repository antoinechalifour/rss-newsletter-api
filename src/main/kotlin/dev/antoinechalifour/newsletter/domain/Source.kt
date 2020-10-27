package dev.antoinechalifour.newsletter.domain

import java.util.UUID

data class Source(val id: UUID, val url: String, val name: String = "") { // TODO remove optional
    companion object {
        fun of(url: String, name: String) = Source(UUID.randomUUID(), url, name)
    }
}
