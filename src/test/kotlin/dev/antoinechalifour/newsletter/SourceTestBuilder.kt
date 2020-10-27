package dev.antoinechalifour.newsletter

import dev.antoinechalifour.newsletter.domain.Source
import java.util.UUID

class SourceTestBuilder {
    companion object {
        fun aSource() = SourceTestBuilder()
    }

    var id = UUID.randomUUID()
    var url = "http://source.com/rss.xml"
    var name = "Source name"

    fun withUrl(theUrl: String) = apply { url = theUrl }
    fun withName(theName: String) = apply { name = theName }
    fun build() = Source(id, url, name)
}
