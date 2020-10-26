package dev.antoinechalifour.newsletter.application

import dev.antoinechalifour.newsletter.domain.Article
import java.time.format.DateTimeFormatter

class ArticleResponse(
    val title: String,
    val url: String,
    val pubDate: String
) {
    companion object {
        fun of(article: Article) = ArticleResponse(
            article.title,
            article.url,
            article.pubDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )

        fun ofAll(articles: List<Article>) = articles.map { of(it) }
    }
}
