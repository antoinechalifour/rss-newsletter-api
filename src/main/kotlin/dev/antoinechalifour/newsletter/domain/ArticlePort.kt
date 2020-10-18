package dev.antoinechalifour.newsletter.domain

interface ArticlePort {
    fun ofSource(source: Source): List<Article>
}
