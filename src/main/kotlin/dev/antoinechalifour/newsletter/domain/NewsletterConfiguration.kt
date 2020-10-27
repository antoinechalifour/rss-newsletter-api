package dev.antoinechalifour.newsletter.domain

import java.util.UUID

class NewsletterConfiguration(val id: UUID, val sources: MutableList<Source> = mutableListOf()) {
    fun newSource(url: String, name: String) {
        sources.add(Source.of(url, name))
    }
}
