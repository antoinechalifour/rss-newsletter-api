package dev.antoinechalifour.newsletter.application

import com.nhaarman.mockitokotlin2.whenever
import dev.antoinechalifour.newsletter.NewsletterConfigurationTestBuilder.Companion.aNewsletterConfiguration
import dev.antoinechalifour.newsletter.application.NewsletterConfigurationController.Companion.HARDCODED_USER_ID
import dev.antoinechalifour.newsletter.basicAuth
import dev.antoinechalifour.newsletter.usecase.CreateNewsletterConfiguration
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
internal class NewsletterConfigurationControllerPostTest : ApiIntegrationTest() {

    @MockBean
    private lateinit var createNewsletterConfiguration: CreateNewsletterConfiguration

    @Test
    fun `should not be accessible without authentication`() {
        checkAuthentication { post("/api/v1/newsletter-configuration") }
    }

    @Test
    fun `returns a newly created newsletter configuration`() {
        // Given
        val newsletterConfiguration = aNewsletterConfiguration().build()
        whenever(createNewsletterConfiguration(HARDCODED_USER_ID)).thenReturn(newsletterConfiguration)

        // When Then
        mockMvc.post("/api/v1/newsletter-configuration") { basicAuth("admin", "passwd") }
            .andExpect {
                status { isCreated }
                jsonPath("$.id", equalTo(newsletterConfiguration.id.toString()))
                jsonPath("$.sources", equalTo(emptyList<SourceResponse>()))
                content { contentType(MediaType.APPLICATION_JSON) }
            }
    }
}
