package dev.antoinechalifour.newsletter.infrastructure.adapter

import dev.antoinechalifour.newsletter.RecipientTestBuilder.Companion.aRecipient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

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
}
