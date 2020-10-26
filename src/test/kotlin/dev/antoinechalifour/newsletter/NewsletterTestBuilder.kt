package dev.antoinechalifour.newsletter

import dev.antoinechalifour.newsletter.RecipientTestBuilder.Companion.aRecipient
import dev.antoinechalifour.newsletter.domain.Article
import dev.antoinechalifour.newsletter.domain.Newsletter

class NewsletterTestBuilder {

    companion object {
        fun aNewsletter() = NewsletterTestBuilder()
    }

    private var recipient = aRecipient().build()
    private var articles = emptyList<Article>()

    fun withArticles(vararg theArticles: Article) = apply { articles = theArticles.toList() }
    fun build() = Newsletter(recipient, articles)
}
