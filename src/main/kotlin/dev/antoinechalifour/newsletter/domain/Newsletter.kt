package dev.antoinechalifour.newsletter.domain

import java.time.Clock
import java.time.LocalDateTime
import java.util.UUID

data class Newsletter(
    val id: UUID,
    val newsletterConfigurationId: UUID,
    val recipient: Recipient,
    val articles: List<Article>,
    val sentAt: LocalDateTime
) {
    companion object {
        fun forToday(
            recipient: Recipient,
            newsletterConfigurationId: UUID,
            articles: List<Article>,
            clock: Clock
        ) = Newsletter(
            UUID.randomUUID(),
            newsletterConfigurationId,
            recipient,
            articles.filter { it.publishedAfter(yesterday(clock)) },
            LocalDateTime.now(clock)
        )

        private fun yesterday(clock: Clock) =
            LocalDateTime.now(clock).minusDays(1).withHour(12).withMinute(30)
    }

    fun isWorthSending() = articles.isNotEmpty()
}
