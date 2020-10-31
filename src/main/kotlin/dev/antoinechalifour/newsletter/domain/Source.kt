package dev.antoinechalifour.newsletter.domain

import java.util.UUID

class Source(val id: UUID, val url: String, val name: String) {
    companion object {
        fun of(url: String, name: String) = Source(UUID.randomUUID(), url, name)
    }
}
