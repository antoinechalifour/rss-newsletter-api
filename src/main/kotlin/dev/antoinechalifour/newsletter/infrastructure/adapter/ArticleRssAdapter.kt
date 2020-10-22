package dev.antoinechalifour.newsletter.infrastructure.adapter

import dev.antoinechalifour.newsletter.domain.Article
import dev.antoinechalifour.newsletter.domain.ArticlePort
import dev.antoinechalifour.newsletter.domain.Source
import dev.antoinechalifour.newsletter.infrastructure.http.rss.RssFeedXml
import dev.antoinechalifour.newsletter.infrastructure.http.rss.RssService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class ArticleRssAdapter(val rssService: RssService) : ArticlePort {
    override fun ofSource(source: Source): List<Article> =
        rssService.getRss(source.url)
            .execute().body()
            ?.channel?.items?.map(this::fromXmlItem)
            ?: emptyList()

    private fun fromXmlItem(item: RssFeedXml.Channel.Item) = Article(item.title, item.link, item.pubDate.parse())
}

private fun String.parse() = LocalDateTime.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(this))
