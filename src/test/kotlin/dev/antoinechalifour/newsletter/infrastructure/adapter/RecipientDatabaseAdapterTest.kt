package dev.antoinechalifour.newsletter.infrastructure.adapter

import dev.antoinechalifour.newsletter.RecipientTestBuilder.Companion.aRecipient
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID

@SpringBootTest
internal class RecipientDatabaseAdapterTest : DatabaseAdapterTest() {

    @Autowired
    private lateinit var recipientDatabaseAdapter: RecipientDatabaseAdapter

    @BeforeEach
    fun setup() {
        cleanDatabase()
    }

    @Test
    fun `retrieves a recipient using its ID`() {
        // Given
        val theRecipient = aRecipient().build()

        // When
        recipientDatabaseAdapter.save(theRecipient)

        // Then
        assertThat(recipientDatabaseAdapter.ofId(theRecipient.id))
            .usingRecursiveComparison()
            .isEqualTo(theRecipient)
    }

    @Test
    fun `throws an exception when the recipient is not found using its ID`() {
        val idWithoutRecipient = UUID.randomUUID()

        assertThatThrownBy { recipientDatabaseAdapter.ofId(idWithoutRecipient) }
            .isInstanceOf(NoSuchElementException::class.java)
            .hasMessage("Recipient $idWithoutRecipient was not found")
    }

    @Test
    fun `retrieves a recipient using its email`() {
        // Given
        val theRecipient = aRecipient().build()

        // When
        recipientDatabaseAdapter.save(theRecipient)

        // Then
        assertThat(recipientDatabaseAdapter.ofEmail(theRecipient.email))
            .usingRecursiveComparison()
            .isEqualTo(theRecipient)
    }

    @Test
    fun `throws an exception when the recipient is not found using its email`() {
        val emailWithoutRecipient = "some.unkown@email.com"

        assertThatThrownBy { recipientDatabaseAdapter.ofEmail(emailWithoutRecipient) }
            .isInstanceOf(NoSuchElementException::class.java)
            .hasMessage("Recipient $emailWithoutRecipient was not found")
    }
}
