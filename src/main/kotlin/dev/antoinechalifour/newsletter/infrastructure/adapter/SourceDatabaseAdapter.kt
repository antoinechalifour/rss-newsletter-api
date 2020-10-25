package dev.antoinechalifour.newsletter.infrastructure.adapter

import dev.antoinechalifour.newsletter.domain.Source
import dev.antoinechalifour.newsletter.domain.SourcePort
import dev.antoinechalifour.newsletter.infrastructure.database.SourceDatabase
import dev.antoinechalifour.newsletter.infrastructure.database.SourceRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class SourceDatabaseAdapter(val sourceRepository: SourceRepository) : SourcePort {
    override fun all() = sourceRepository.findAll().map { it.toSource() }

    override fun save(source: Source) {
        sourceRepository.save(SourceDatabase.of(source))
    }
}
