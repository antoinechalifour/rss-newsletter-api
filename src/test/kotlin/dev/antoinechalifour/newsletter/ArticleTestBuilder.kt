package dev.antoinechalifour.newsletter

import dev.antoinechalifour.newsletter.domain.Article
import java.time.Clock
import java.time.LocalDateTime

class ArticleTestBuilder(clock: Clock) {
    val today = LocalDateTime.now(clock)
    var title = "article title"
    var url = "http://article.com"
    var pubDate = today

    fun fromYesterdayBefore1230() = apply { pubDate = today.minusDays(1).withHour(12).withMinute(29) }
    fun withUrl(theUrl: String) = apply { url = theUrl }
    fun build() = Article(title, url, pubDate)
}
