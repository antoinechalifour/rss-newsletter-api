package dev.antoinechalifour.newsletter.infrastructure

import dev.antoinechalifour.newsletter.domain.Article
import dev.antoinechalifour.newsletter.domain.ArticlePort
import dev.antoinechalifour.newsletter.domain.Source
import org.springframework.stereotype.Component

@Component
class ArticleRssAdapter(val rssService: RssService) : ArticlePort {
    override fun ofSource(source: Source): List<Article> =
        rssService.getRss(source.url)
            .execute().body()
            ?.channel?.items?.map(this::fromXmlItem)
            ?: emptyList()

    private fun fromXmlItem(item: RssFeedXml.Channel.Item) = Article(item.title, item.link, item.pubDate)
}