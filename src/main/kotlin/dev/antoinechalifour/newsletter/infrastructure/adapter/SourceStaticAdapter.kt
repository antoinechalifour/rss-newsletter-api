package dev.antoinechalifour.newsletter.infrastructure.adapter

import dev.antoinechalifour.newsletter.domain.Source
import dev.antoinechalifour.newsletter.domain.SourcePort
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class SourceStaticAdapter : SourcePort {
    override fun ofId(id: UUID): Source {
        TODO("Not yet implemented")
    }

    override fun all() = listOf(
        Source(UUID.randomUUID(), "https://overreacted.io/rss.xml"),
        Source(UUID.randomUUID(), "https://blog.octo.com/feed/"),
        Source(UUID.randomUUID(), "https://www.lemonde.fr/rss/une.xml")
    )

    override fun save(source: Source) {
        TODO("Not yet implemented")
    }
}
