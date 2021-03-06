package dev.antoinechalifour.newsletter.infrastructure.adapter

import dev.antoinechalifour.newsletter.domain.ArticlePort
import dev.antoinechalifour.newsletter.domain.Source
import dev.antoinechalifour.newsletter.infrastructure.http.rss.RssService
import org.springframework.stereotype.Component

@Component
class ArticleRssAdapter(val rssService: RssService) : ArticlePort {
    override fun ofSource(source: Source) = try {
        val response = rssService.getRss(source.url).execute()

        response.body()
            ?.articles(source.name)
            ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }
}
