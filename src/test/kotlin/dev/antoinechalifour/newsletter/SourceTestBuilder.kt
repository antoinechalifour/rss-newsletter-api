package dev.antoinechalifour.newsletter

import dev.antoinechalifour.newsletter.domain.Source
import java.util.UUID

class SourceTestBuilder {
    var id = UUID.randomUUID()
    var url = "http://source.com/rss.xml"

    fun build() = Source(id, url)

    fun withUrl(theUrl: String) = apply { url = theUrl }
}
