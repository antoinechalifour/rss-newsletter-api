package dev.antoinechalifour.newsletter.infrastructure.http.rss

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
class RssFeedXml {
    @field:Element(name = "channel")
    lateinit var channel: Channel

    @Root(name = "channel", strict = false)
    class Channel {
        @field:ElementList(name = "item", inline = true)
        lateinit var items: List<Item>

        @Root(name = "item", strict = false)
        class Item {
            @field:Element(name = "pubDate")
            lateinit var pubDate: String

            @field:Element(name = "title")
            lateinit var title: String

            @field:Element(name = "link")
            lateinit var link: String
        }
    }
}
