package dev.antoinechalifour.newsletter.acceptance

import dev.antoinechalifour.newsletter.asTestResourceFileContent
import dev.antoinechalifour.newsletter.basicAuth
import dev.antoinechalifour.newsletter.domain.NewsletterConfiguration
import dev.antoinechalifour.newsletter.domain.NewsletterConfigurationPort
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
internal class AddNewSourceToNewsletterConfigurationAcceptanceTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var newsletterConfigurationPort: NewsletterConfigurationPort

    private lateinit var newsletterConfigurationId: UUID

    @BeforeEach
    fun setup() {
        newsletterConfigurationId = UUID.randomUUID()
        val newsletterConfiguration = NewsletterConfiguration(newsletterConfigurationId)
        newsletterConfigurationPort.save(newsletterConfiguration)
    }

    @Test
    fun `adds a new source to the existing newsletter configuration`() {
        // When
        mockMvc.post("/api/v1/newsletter-configuration/$newsletterConfigurationId/sources") {
            basicAuth("admin", "passwd")
            contentType = MediaType.APPLICATION_JSON
            content = "/test-http/create-source.json".asTestResourceFileContent()
        }

        // Then
        val newsletterConfiguration = newsletterConfigurationPort.ofId(newsletterConfigurationId)

        assertThat(newsletterConfiguration.sources).hasSize(1) // TODO: custom assertions ?
        assertThat(newsletterConfiguration.sources[0]).hasFieldOrPropertyWithValue("url", "http://tech.com/rss.xml")
    }
}
