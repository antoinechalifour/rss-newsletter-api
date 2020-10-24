package dev.antoinechalifour.newsletter.acceptance

import dev.antoinechalifour.newsletter.asTestResourceFileContent
import dev.antoinechalifour.newsletter.basicAuth
import dev.antoinechalifour.newsletter.infrastructure.database.SourceRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
internal class AddSourceAcceptanceTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var sourceRepository: SourceRepository

    @BeforeEach
    fun setup() {
        sourceRepository.deleteAll()
    }

    @Test
    fun `creates a new source`() {
        // When
        mockMvc.post("/api/v1/sources") {
            basicAuth("admin", "passwd")
            contentType = MediaType.APPLICATION_JSON
            content = "/test-http/create-source.json".asTestResourceFileContent()
        }

        // Then
        val sources = sourceRepository.findAll()

        assertThat(sources).hasSize(1)
        assertThat(sources[0]).hasFieldOrPropertyWithValue("url", "http://tech.com/rss.xml")
    }
}
