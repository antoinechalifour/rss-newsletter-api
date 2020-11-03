package dev.antoinechalifour.newsletter.usecase

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.antoinechalifour.newsletter.RecipientTestBuilder.Companion.aRecipient
import dev.antoinechalifour.newsletter.application.UserDetails
import dev.antoinechalifour.newsletter.domain.RecipientPort
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat as assertThat

internal class SynchronizeAccountTest {

    private lateinit var recipientPort: RecipientPort

    @BeforeEach
    fun setup() {
        recipientPort = mock()
    }

    @Test
    fun `updates an existing recipient`() {
        // Given
        val existingRecipient = aRecipient().build()
        val userDetails = UserDetails(existingRecipient.email, "New name")
        val synchronizeAccount = SynchronizeAccount(recipientPort)
        whenever(recipientPort.ofEmail(existingRecipient.email)).thenReturn(existingRecipient)

        // When
        val recipient = synchronizeAccount(userDetails)

        // Then
        assertThat(recipient.id).isEqualTo(existingRecipient.id)
        assertThat(recipient.email).isEqualTo(existingRecipient.email)
        assertThat(recipient.name).isEqualTo("New name")
        verify(recipientPort).save(recipient)
    }

    @Test
    fun `creates a new recipient`() {
        // Given
        val userDetails = UserDetails("new.recipient@email.com", "John Doe")
        val synchronizeAccount = SynchronizeAccount(recipientPort)
        whenever(recipientPort.ofEmail("new.recipient@email.com"))
            .thenThrow(NoSuchElementException())

        // When
        val recipient = synchronizeAccount(userDetails)

        // Then
        assertThat(recipient.email).isEqualTo("new.recipient@email.com")
        assertThat(recipient.name).isEqualTo("John Doe")
        verify(recipientPort).save(recipient)
    }
}
