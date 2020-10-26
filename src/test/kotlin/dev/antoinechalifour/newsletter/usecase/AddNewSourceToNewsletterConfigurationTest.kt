package dev.antoinechalifour.newsletter.usecase

import com.nhaarman.mockitokotlin2.check
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.antoinechalifour.newsletter.NewsletterConfigurationTestBuilder.Companion.aNewsletterConfiguration
import dev.antoinechalifour.newsletter.SourceTestBuilder.Companion.aSource
import dev.antoinechalifour.newsletter.asserts.NewsletterConfigurationAssert.Companion.assertThat
import dev.antoinechalifour.newsletter.domain.NewsletterConfiguration
import dev.antoinechalifour.newsletter.domain.NewsletterConfigurationPort
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AddNewSourceToNewsletterConfigurationTest {
    private lateinit var newsletterConfigurationPort: NewsletterConfigurationPort
    private lateinit var newsletterConfiguration: NewsletterConfiguration

    @BeforeEach
    fun setup() {
        newsletterConfigurationPort = mock()

        val source = aSource().withUrl("http://existing.source.com/feed.xml")
        newsletterConfiguration = aNewsletterConfiguration().withSources(source).build()
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
            check { assertThat(it).hasSourceMatchingUrl(url) }
        )
    }
}
