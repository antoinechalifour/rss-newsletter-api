package dev.antoinechalifour.newsletter.usecase

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import dev.antoinechalifour.newsletter.domain.NewsletterConfigurationPort
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CreateNewsletterConfigurationTest {
    private lateinit var newsletterConfigurationPort: NewsletterConfigurationPort

    @BeforeEach
    fun setup() {
        newsletterConfigurationPort = mock()
    }

    @Test
    fun `creates and save a new newsletter configuration`() {
        // Given
        val createNewsletterConfiguration = CreateNewsletterConfiguration(newsletterConfigurationPort)

        // When
        val newsletterConfiguration = createNewsletterConfiguration()

        // Then
        assertThat(newsletterConfiguration.id).isNotNull()
        verify(newsletterConfigurationPort).save(newsletterConfiguration)
    }
}
