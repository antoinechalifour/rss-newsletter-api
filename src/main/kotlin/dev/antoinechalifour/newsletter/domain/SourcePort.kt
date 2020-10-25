package dev.antoinechalifour.newsletter.domain

import java.util.UUID

interface SourcePort {
    fun all(): List<Source>
    fun save(source: Source)
}
