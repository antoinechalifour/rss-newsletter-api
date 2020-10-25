package dev.antoinechalifour.newsletter.application

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.antoinechalifour.newsletter.asTestResourceFileContent
import dev.antoinechalifour.newsletter.basicAuth
import dev.antoinechalifour.newsletter.domain.NewsletterConfiguration
import dev.antoinechalifour.newsletter.domain.Source
import dev.antoinechalifour.newsletter.usecase.AddNewSource
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
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
    private lateinit var newsletterConfiguration: NewsletterConfiguration
    private val newSourceUrl = "http://tech.com/rss.xml"

    @MockBean
    private lateinit var addNewSource: AddNewSource

    @Autowired
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup() {
        newsletterConfiguration = aNewsletterConfiguration()
    }


    private fun aNewsletterConfiguration() = NewsletterConfiguration(
        UUID.randomUUID(), mutableListOf(theSourceWithUrl(newSourceUrl))
    )

    private fun theSourceWithUrl(url: String) = Source(UUID.randomUUID(), url)

    @Test
    fun `returns the created source`() { // TODO: rename this test
        // Given
        val newSource = newsletterConfiguration.sources[0]
        whenever(addNewSource.invoke(newsletterConfiguration.id.toString(), newSourceUrl))
            .thenReturn(newsletterConfiguration)

        // When
        mockMvc.post("/api/v1/newsletter-configuration/${newsletterConfiguration.id}/sources") {
            basicAuth("admin", "passwd")
            contentType = MediaType.APPLICATION_JSON
            content = "/test-http/create-source.json".asTestResourceFileContent()
        }.andExpect {
            jsonPath("$.id", equalTo(newsletterConfiguration.id.toString()))
            jsonPath("$.sources[0].id", equalTo(newSource.id.toString()))
            jsonPath("$.sources[0].url", equalTo(newSource.url))
//            status { isCreated }
//            content { contentType(MediaType.APPLICATION_JSON) }
        }

        // Then
        verify(addNewSource).invoke(newsletterConfiguration.id.toString(), newSourceUrl)
    }
}
