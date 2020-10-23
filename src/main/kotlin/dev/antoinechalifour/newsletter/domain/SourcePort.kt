package dev.antoinechalifour.newsletter.domain

import java.util.UUID

interface SourcePort {
    fun ofId(id: UUID): Source
    fun all(): List<Source>
    fun save(source: Source)
}
