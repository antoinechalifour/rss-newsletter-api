package dev.antoinechalifour.newsletter.application

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.antoinechalifour.newsletter.asTestResourceFileContent
import dev.antoinechalifour.newsletter.basicAuth
import dev.antoinechalifour.newsletter.domain.Source
import dev.antoinechalifour.newsletter.usecase.AddNewSource
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
internal class SourceControllerPostTest {

    @MockBean
    private lateinit var addNewSource: AddNewSource

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `returns the created source`() {
        // Given
        val source = Source(UUID.randomUUID(), "http://tech.com/rss.xml")
        whenever(addNewSource.invoke("http://tech.com/rss.xml")).thenReturn(source)

        // When
        mockMvc.post("/api/v1/sources") {
            basicAuth("admin", "passwd")
            contentType = MediaType.APPLICATION_JSON
            content = "/test-http/create-source.json".asTestResourceFileContent()
        }.andExpect {
            jsonPath("$.id", equalTo(source.id.toString()))
            jsonPath("$.url", equalTo(source.url))
            content { contentType(MediaType.APPLICATION_JSON) }
            status { isCreated }
        }

        // Then
        verify(addNewSource).invoke(source.url)
    }
}
