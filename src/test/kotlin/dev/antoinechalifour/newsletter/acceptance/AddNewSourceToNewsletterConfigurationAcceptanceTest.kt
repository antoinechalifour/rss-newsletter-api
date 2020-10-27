package dev.antoinechalifour.newsletter.acceptance

import dev.antoinechalifour.newsletter.asTestResourceFileContent
import dev.antoinechalifour.newsletter.asserts.NewsletterConfigurationAssert.Companion.assertThat
import dev.antoinechalifour.newsletter.basicAuth
import dev.antoinechalifour.newsletter.domain.NewsletterConfiguration
import dev.antoinechalifour.newsletter.domain.NewsletterConfigurationPort
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
internal class AddNewSourceToNewsletterConfigurationAcceptanceTest : AcceptanceTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var newsletterConfigurationPort: NewsletterConfigurationPort

    private lateinit var newsletterConfigurationId: UUID

    @BeforeEach
    fun setup() {
        cleanupDatabase()

        newsletterConfigurationId = UUID.randomUUID()
        NewsletterConfiguration(newsletterConfigurationId).run {
            newsletterConfigurationPort.save(this)
        }
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
        newsletterConfigurationPort.ofId(newsletterConfigurationId)
            .let {
                assertThat(it)
                    .hasSourceWithName("Source name")
                    .hasSourceMatchingUrl("http://tech.com/rss.xml")
            }
    }
}
