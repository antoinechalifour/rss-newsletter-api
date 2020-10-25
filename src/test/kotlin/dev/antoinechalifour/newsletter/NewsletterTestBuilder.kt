package dev.antoinechalifour.newsletter

import dev.antoinechalifour.newsletter.domain.Article
import dev.antoinechalifour.newsletter.domain.Newsletter

class NewsletterTestBuilder {

    private var recipient = RecipientTestBuilder().build()
    private var articles = emptyList<Article>()

    fun build() = Newsletter(recipient, articles)
}
