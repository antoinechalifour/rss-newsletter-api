package dev.antoinechalifour.newsletter.domain

import java.time.LocalDateTime

data class Article(val title: String, val url: String, val pubDate: LocalDateTime) {
    fun publishedAfter(date: LocalDateTime) = pubDate.isAfter(date)
}
