package dev.antoinechalifour.newsletter.infrastructure.adapter

import dev.antoinechalifour.newsletter.ArticleTestBuilder.Companion.anArticle
import dev.antoinechalifour.newsletter.NewsletterTestBuilder.Companion.aNewsletter
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.UUID

@SpringBootTest
internal class NewsletterDatabaseAdapterTest : DatabaseAdapterTest() {

    @Autowired
    private lateinit var newsletterDatabaseAdapter: NewsletterDatabaseAdapter

    private val clock = Clock.fixed(now(), ZoneId.of("Europe/Paris"))

    @BeforeEach
    fun setup() {
        cleanDatabase()
    }

    @Test
    fun `saves a newsletter to the database`() {
        // Given
        val firstArticle = anArticle(clock).withUrl("http://source.com/first-article").build()
        val secondArticle = anArticle(clock).withUrl("http://source.com/second-article").build()
        val newsletter = aNewsletter().withArticles(firstArticle, secondArticle).build()

        // When
        newsletterDatabaseAdapter.save(newsletter)

        // Then
        assertThat(newsletterDatabaseAdapter.ofId(newsletter.id))
            .usingRecursiveComparison()
            .isEqualTo(newsletter)
    }

    @Test
    fun `throws an exception when the newsletter is not found`() {
        val idWithoutNewsletter = UUID.randomUUID()

        assertThatThrownBy { newsletterDatabaseAdapter.ofId(idWithoutNewsletter) }
            .isInstanceOf(NoSuchElementException::class.java)
            .hasMessage("Newsletter $idWithoutNewsletter was not found")
    }

    private fun now() = Instant.parse("2020-10-19T17:30:00.00Z")
}
