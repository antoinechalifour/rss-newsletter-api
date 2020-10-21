package dev.antoinechalifour.newsletter.infrastructure

import dev.antoinechalifour.newsletter.domain.Article
import dev.antoinechalifour.newsletter.domain.ArticlePort
import dev.antoinechalifour.newsletter.domain.Source
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class ArticleRssAdapter(val rssService: RssService) : ArticlePort {
    override fun ofSource(source: Source): List<Article> {
        println("Fetching source ${source.url}")
        val articles = rssService.getRss(source.url)
            .execute().body()
            ?.channel?.items?.map(this::fromXmlItem)
            ?: emptyList()

        println("Done fetching ${source.url} (articles: ${articles.size})")
        return articles
    }

    private fun fromXmlItem(item: RssFeedXml.Channel.Item) = Article(item.title, item.link, item.pubDate.parse())
}

private fun String.parse() = LocalDateTime.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(this))
