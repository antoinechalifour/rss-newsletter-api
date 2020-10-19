package dev.antoinechalifour.newsletter.domain

data class Newsletter(val recipient: Recipient, val articles: List<Article>) {
}
