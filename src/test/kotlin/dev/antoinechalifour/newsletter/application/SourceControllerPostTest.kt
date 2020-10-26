package dev.antoinechalifour.newsletter.application

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.antoinechalifour.newsletter.NewsletterConfigurationTestBuilder.Companion.aNewsletterConfiguration
import dev.antoinechalifour.newsletter.SourceTestBuilder.Companion.aSource
import dev.antoinechalifour.newsletter.asTestResourceFileContent
import dev.antoinechalifour.newsletter.basicAuth
import dev.antoinechalifour.newsletter.domain.NewsletterConfiguration
import dev.antoinechalifour.newsletter.domain.Source
import dev.antoinechalifour.newsletter.usecase.AddNewSourceToNewsletterConfiguration
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
internal class SourceControllerPostTest : ApiIntegrationTest() {

    private lateinit var newsletterConfiguration: NewsletterConfiguration
    private lateinit var newSource: Source

    @MockBean
    private lateinit var addNewSourceToNewsletterConfiguration: AddNewSourceToNewsletterConfiguration

    @BeforeEach
    fun setup() {
        newSource = aSource().withUrl("http://tech.com/rss.xml").build()
        newsletterConfiguration = aNewsletterConfiguration().withSources(newSource).build()
    }

    @Test
    fun `should not be accessible without authentication`() {
        checkAuthentication { post("/api/v1/newsletter-configuration/${newsletterConfiguration.id}/sources") }
    }

    @Test
    fun `returns the updated newsletter configuration containing the new source`() {
        // Given
        whenever(
            addNewSourceToNewsletterConfiguration.invoke(newsletterConfiguration.id.toString(), newSource.url)
        ).thenReturn(newsletterConfiguration)

        // When
        mockMvc.post("/api/v1/newsletter-configuration/${newsletterConfiguration.id}/sources") {
            basicAuth("admin", "passwd")
            contentType = MediaType.APPLICATION_JSON
            content = "/test-http/create-source.json".asTestResourceFileContent()
        }.andExpect {
            status { isCreated }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id", equalTo(newsletterConfiguration.id.toString()))
            jsonPath("$.sources[0].id", equalTo(newSource.id.toString()))
            jsonPath("$.sources[0].url", equalTo(newSource.url))
        }

        // Then
        verify(addNewSourceToNewsletterConfiguration).invoke(newsletterConfiguration.id.toString(), newSource.url)
    }

    @Test
    fun `returns a 404 when the newsletter configuration is not found`() {
        // Given
        whenever(addNewSourceToNewsletterConfiguration.invoke(any(), any())).thenThrow(NoSuchElementException())

        // When
        mockMvc.post("/api/v1/newsletter-configuration/not-a-valid-id/sources") {
            basicAuth("admin", "passwd")
            contentType = MediaType.APPLICATION_JSON
            content = "/test-http/create-source.json".asTestResourceFileContent()
        }.andExpect {
            status { isNotFound }
        }
    }
}
