package dev.antoinechalifour.newsletter.usecase

import com.nhaarman.mockitokotlin2.check
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import dev.antoinechalifour.newsletter.domain.SourcePort
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AddNewSourceTest {
    private lateinit var sourcePort: SourcePort

    @BeforeEach
    fun setup() {
        sourcePort = mock()
    }

    @Test
    fun `creates and save the source`() {
        // Given
        val addNewSource = AddNewSource(sourcePort)
        val url = "http://source.com/rss.xml"

        // When
        addNewSource(url)

        // Then
        verify(sourcePort).save(
            check {
                assertThat(it.id).isNotNull()
                assertThat(it.url).isEqualTo(url)
            }
        )
    }
}
