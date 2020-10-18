package dev.antoinechalifour.newsletter.usecase

import dev.antoinechalifour.newsletter.domain.*

class SendNewsletter(
    private val sourcePort: SourcePort,
    private val articlePort: ArticlePort,
    private val newsletterPort: NewsletterPort
) {
    operator fun invoke() {
        val sources = sourcePort.all()
        val articles = sources.map(articlePort::ofSource).flatten()
        val newsletter = Newsletter(articles)

        newsletterPort.send(newsletter)
    }
}