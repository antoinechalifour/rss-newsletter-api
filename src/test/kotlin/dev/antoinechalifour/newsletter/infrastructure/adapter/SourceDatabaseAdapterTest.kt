package dev.antoinechalifour.newsletter.infrastructure.adapter

import dev.antoinechalifour.newsletter.domain.Source
import dev.antoinechalifour.newsletter.infrastructure.database.SourceRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.lang.IllegalStateException
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

    @Test
    fun `saves a source`() {
        // Given
        val source = Source(UUID.randomUUID(), "http://source.com/rss.xml")

        // When
        sourceDatabaseAdapter.save(source)

        // Then
        val sourceFromDatabase = sourceDatabaseAdapter.ofId(source.id)

        assertThat(sourceFromDatabase).isEqualTo(source)
    }

    @Test
    fun `throw an exception when the source is not found`() {
        val idWithoutSource = UUID.randomUUID()

        assertThatThrownBy { sourceDatabaseAdapter.ofId(idWithoutSource) }
            .isInstanceOf(NoSuchElementException::class.java)
            .hasMessage("Source of id $idWithoutSource was not found")
    }

    private fun aNewsSource() = Source(UUID.randomUUID(), "http://news.com/rss.xml")

    private fun aTechSource() = Source(UUID.randomUUID(), "http://tech.com/rss.xml")
}

private fun SourceDatabaseAdapter.save(vararg sources: Source) = sources.forEach { save(it) }
