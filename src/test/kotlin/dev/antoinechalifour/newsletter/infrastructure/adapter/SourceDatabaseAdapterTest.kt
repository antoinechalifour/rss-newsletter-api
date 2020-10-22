package dev.antoinechalifour.newsletter.infrastructure.adapter

import dev.antoinechalifour.newsletter.domain.Source
import dev.antoinechalifour.newsletter.infrastructure.database.SourceDatabase
import dev.antoinechalifour.newsletter.infrastructure.database.SourceRepository
import org.assertj.core.api.Assertions.assertThat
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
        val newsSourceDatabase = aNewsSourceDatabase()
        val techSourceDatabase = aTechSourceDatabase()
        sourceRepository.saveAll(listOf(newsSourceDatabase, techSourceDatabase))

        // When
        val sources = sourceDatabaseAdapter.all()

        // Then
        assertThat(sources).usingRecursiveComparison().isEqualTo(
            listOf(
                theNewsSource(newsSourceDatabase.id),
                theTechSource(techSourceDatabase.id)
            )
        )
    }

    private fun theNewsSource(id: UUID?) = Source(checkNotNull(id), "http://news.com/rss.xml")

    private fun theTechSource(id: UUID?) = Source(checkNotNull(id), "http://tech.com/rss.xml")

    private fun aNewsSourceDatabase() = SourceDatabase().apply {
        id = UUID.randomUUID()
        url = "http://news.com/rss.xml"
    }

    private fun aTechSourceDatabase() = SourceDatabase().apply {
        id = UUID.randomUUID()
        url = "http://tech.com/rss.xml"
    }
}