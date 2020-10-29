package dev.antoinechalifour.newsletter.application

import dev.antoinechalifour.newsletter.domain.Article
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ArticleResponse(
    val title: String,
    val url: String,
    pubDate: LocalDateTime,
    val source: String
) {
    val pubDate: String = pubDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    companion object {
        fun of(article: Article) = ArticleResponse(
            article.title,
            article.url,
            article.pubDate,
            article.source
        )

        fun ofAll(articles: List<Article>) = articles.map { of(it) }
    }
}
