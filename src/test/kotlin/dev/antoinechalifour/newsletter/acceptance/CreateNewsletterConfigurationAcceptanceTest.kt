package dev.antoinechalifour.newsletter.acceptance

import dev.antoinechalifour.newsletter.basicAuth
import dev.antoinechalifour.newsletter.infrastructure.database.NewsletterConfigurationRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class CreateNewsletterConfigurationAcceptanceTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var newsletterConfigurationRepository: NewsletterConfigurationRepository

    @BeforeEach
    fun setup() {
        newsletterConfigurationRepository.deleteAll()
    }

    @Test
    fun `creates a new newsletter configuration`() {
        // When
        mockMvc.post("/api/v1/newsletter-configuration") { basicAuth("admin", "passwd") }

        // Then
        assertThat(newsletterConfigurationRepository.findAll()).hasSize(1)
    }
}
