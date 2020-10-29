package dev.antoinechalifour.newsletter.asserts

import dev.antoinechalifour.newsletter.domain.Article
import dev.antoinechalifour.newsletter.domain.Newsletter
import dev.antoinechalifour.newsletter.domain.Recipient
import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions.assertThat
import java.time.LocalDateTime
import java.util.UUID

class NewsletterAssert(actual: Newsletter) :
    AbstractAssert<NewsletterAssert, Newsletter>(actual, NewsletterAssert::class.java) {

    companion object {
        fun assertThat(actual: Newsletter) = NewsletterAssert(actual)
    }

    fun isSentTo(theRecipient: Recipient) = apply {
        assertThat(actual.recipient)
            .usingRecursiveComparison()
            .isEqualTo(theRecipient)
    }

    fun hasOnlyTheArticles(vararg theArticles: Article) {
        assertThat(actual.articles)
            .usingRecursiveComparison()
            .isEqualTo(theArticles.toList())
    }

    fun hasRecipient(theRecipient: Recipient) = isSentTo(theRecipient)

    fun hasNewsletterConfigurationId(newsletterConfigurationId: UUID) {
        assertThat(actual.newsletterConfigurationId).isEqualTo(newsletterConfigurationId)
    }

    fun hasBeenSentAt(date: LocalDateTime) {
        if (!actual.sentAt.isEqual(date))
            failWithMessage(
                "Expected newsletter to be sent at %s but was sent at %s",
                date,
                actual.sentAt
            )
    }
}
