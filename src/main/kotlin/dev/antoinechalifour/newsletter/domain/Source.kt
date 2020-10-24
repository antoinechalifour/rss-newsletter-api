package dev.antoinechalifour.newsletter.domain

import java.util.UUID

data class Source(val id: UUID, val url: String) {
    companion object {
        fun of(url: String) = Source(UUID.randomUUID(), url)
    }
}
