package dev.antoinechalifour.newsletter.infrastructure.database

import dev.antoinechalifour.newsletter.domain.Source
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "source")
open class SourceDatabase {
    @Id
    open var id: UUID? = null
    open var url: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    open var newsletterConfiguration: NewsletterConfigurationDatabase? = null

    fun toSource() = Source(checkNotNull(id), checkNotNull(url))

    companion object {
        fun of(source: Source) = SourceDatabase().apply {
            id = source.id
            url = source.url
        }
    }
}
