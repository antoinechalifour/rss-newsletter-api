package dev.antoinechalifour.newsletter

import dev.antoinechalifour.newsletter.domain.Article
import java.time.Clock
import java.time.LocalDateTime

class ArticleTestBuilder(clock: Clock) {

    companion object {
        fun anArticle(clock: Clock) = ArticleTestBuilder(clock)
    }

    val today = LocalDateTime.now(clock)
    var title = "article title"
    var url = "http://article.com"
    var pubDate = today
    var source = "Source name"

    fun fromYesterdayBefore1230() = apply { pubDate = today.minusDays(1).withHour(12).withMinute(29) }
    fun withUrl(theUrl: String) = apply { url = theUrl }
    fun withTitle(theTitle: String) = apply { title = theTitle }
    fun build() = Article(title, url, pubDate, source)
}
