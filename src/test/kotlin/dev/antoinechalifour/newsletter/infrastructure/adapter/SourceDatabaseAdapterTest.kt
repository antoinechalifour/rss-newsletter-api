package dev.antoinechalifour.newsletter.infrastructure.adapter

import dev.antoinechalifour.newsletter.domain.Source
import dev.antoinechalifour.newsletter.infrastructure.database.SourceRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID

@SpringBootTest
internal class SourceDatabaseAdapterTest {

    @Autowired
    private lateinit var sourceDatabaseAdapter: SourceDatabaseAdapter

    @Autowired
    private lateinit var sourceRepository: SourceRepository

    @BeforeEach
    fun setup() {
        sourceRepository.deleteAll()
    }

    @Test
    fun `returns all the sources`() {
        // Given
        val newsSource = aNewsSource()
        val techSource = aTechSource()

        // When
        sourceDatabaseAdapter.save(newsSource, techSource)

        // Then
        assertThat(sourceDatabaseAdapter.all())
            .usingRecursiveComparison()
            .isEqualTo(listOf(newsSource, techSource))
    }

    private fun aNewsSource() = Source(UUID.randomUUID(), "http://news.com/rss.xml")

    private fun aTechSource() = Source(UUID.randomUUID(), "http://tech.com/rss.xml")
}

private fun SourceDatabaseAdapter.save(vararg sources: Source) = sources.forEach { save(it) }
