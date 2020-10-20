package dev.antoinechalifour.newsletter.domain

import java.time.Clock
import java.time.LocalDateTime

data class Newsletter(val recipient: Recipient, val articles: List<Article>) {
    companion object {
        fun forToday(
            recipient: Recipient,
            articles: List<Article>,
            clock: Clock
        ) = Newsletter(recipient, articles.filter { it.publishedAfter(yesterday(clock)) })

        private fun yesterday(clock: Clock) =
            LocalDateTime.now(clock).minusDays(1).withHour(12).withMinute(30)
    }

    fun isWorthSending() = articles.isNotEmpty()
}
