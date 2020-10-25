package dev.antoinechalifour.newsletter.usecase

import com.nhaarman.mockitokotlin2.check
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.antoinechalifour.newsletter.domain.NewsletterConfiguration
import dev.antoinechalifour.newsletter.domain.NewsletterConfigurationPort
import dev.antoinechalifour.newsletter.domain.Source
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

internal class AddNewSourceToNewsletterConfigurationTest {
    private lateinit var newsletterConfigurationPort: NewsletterConfigurationPort
    private lateinit var newsletterConfiguration: NewsletterConfiguration

    @BeforeEach
    fun setup() {
        newsletterConfigurationPort = mock()
        newsletterConfiguration = aNewsletterConfiguration()
    }

    @Test
    fun `creates a new source for the requested newsletter configuration`() {
        // Given
        whenever(newsletterConfigurationPort.ofId(newsletterConfiguration.id))
            .thenReturn(newsletterConfiguration)

        val addNewSource = AddNewSourceToNewsletterConfiguration(newsletterConfigurationPort)
        val url = "http://source.com/rss.xml"

        // When
        addNewSource(newsletterConfiguration.id.toString(), url)

        // Then
        verify(newsletterConfigurationPort).save(
            check {
                // TODO: refactor, using a custom assertion ?
                assertThat(it.sources).hasSize(2)
                assertThat(it.sources[1]).hasFieldOrPropertyWithValue("url", url)
            }
        )
    }

    private fun aNewsletterConfiguration() = NewsletterConfiguration(UUID.randomUUID(), mutableListOf(aSource()))

    private fun aSource() = Source(UUID.randomUUID(), "http://old.source.com/rss.xml")
}
