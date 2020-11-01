package dev.antoinechalifour.newsletter.acceptance

import dev.antoinechalifour.newsletter.RecipientTestBuilder.Companion.aRecipient
import dev.antoinechalifour.newsletter.application.NewsletterConfigurationController.Companion.HARDCODED_USER_ID
import dev.antoinechalifour.newsletter.bearerToken
import dev.antoinechalifour.newsletter.domain.RecipientPort
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
class CreateNewsletterConfigurationAcceptanceTest : AcceptanceTest() {

    private val authorizedToken = "my-token"

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var recipientPort: RecipientPort

    @BeforeEach
    fun setup() {
        cleanupDatabase()
        authorizeForToken(authorizedToken)

        recipientPort.save(aRecipient().withId(HARDCODED_USER_ID).build())
    }

    @Test
    fun `creates a new newsletter configuration`() {
        // When
        mockMvc.post("/api/v1/newsletter-configuration") { bearerToken(authorizedToken) }

        // Then
        assertThat(newsletterConfigurationRepository.findAll()).hasSize(1)
    }
}
