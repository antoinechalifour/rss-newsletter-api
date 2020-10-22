package dev.antoinechalifour.newsletter.infrastructure.database

import dev.antoinechalifour.newsletter.domain.Source
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "source")
open class SourceDatabase {
    @Id
    open var id: UUID? = null
    open var url: String? = null

    fun toSource() = Source(checkNotNull(id), checkNotNull(url))
}
