package dev.antoinechalifour.newsletter.infrastructure

import dev.antoinechalifour.newsletter.domain.Source
import dev.antoinechalifour.newsletter.domain.SourcePort
import org.springframework.stereotype.Component

@Component
class SourceStaticAdapter : SourcePort {
    override fun all() = listOf(
        Source("https://overreacted.io/rss.xml"),
        Source("https://blog.octo.com/feed/"),
        Source("https://www.lemonde.fr/rss/une.xml")
    )
}
