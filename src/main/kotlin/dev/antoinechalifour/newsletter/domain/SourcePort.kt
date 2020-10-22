package dev.antoinechalifour.newsletter.domain

interface SourcePort {
    fun all(): List<Source>
    fun save(source: Source)
}
