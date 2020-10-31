package dev.antoinechalifour.newsletter.infrastructure.adapter

import dev.antoinechalifour.newsletter.NewsletterConfigurationTestBuilder.Companion.aNewsletterConfiguration
import dev.antoinechalifour.newsletter.SourceTestBuilder.Companion.aSource
import dev.antoinechalifour.newsletter.domain.NewsletterConfiguration
import dev.antoinechalifour.newsletter.infrastructure.database.RecipientDatabase
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID

@SpringBootTest
internal class NewsletterConfigurationDatabaseAdapterTest : DatabaseAdapterTest() {
    private lateinit var recipient: RecipientDatabase

    @Autowired
    private lateinit var newsletterConfigurationDatabaseAdapter: NewsletterConfigurationDatabaseAdapter

    @BeforeEach
    fun setup() {
        cleanDatabase()

        recipient = RecipientDatabase(
            UUID.randomUUID(),
            "Jane Doe",
            "jane.doe@gmail.com"
        ) // TODO: test builder ?

        recipientRepository.save(recipient)
    }

    @Test
    fun `returns all newsletter configurations from the database`() {
        // Given
        val newsletterConfiguration1 = aNewsletterConfiguration()
            .withRecipientId(checkNotNull(recipient.id))
            .build()
        val newsletterConfiguration2 = aNewsletterConfiguration()
            .withRecipientId(checkNotNull(recipient.id))
            .build()

        // When
        newsletterConfigurationDatabaseAdapter.save(newsletterConfiguration1, newsletterConfiguration2)

        // Then
        assertThat(newsletterConfigurationDatabaseAdapter.all()).hasSize(2)
    }

    @Test
    fun `saves the configuration to the database`() {
        // Given
        val newsletterConfiguration = aNewsletterConfiguration()
            .withRecipientId(checkNotNull(recipient.id))
            .build()

        // When
        newsletterConfigurationDatabaseAdapter.save(newsletterConfiguration)

        // Then
        assertThat(newsletterConfigurationDatabaseAdapter.ofId(newsletterConfiguration.id))
            .usingRecursiveComparison()
            .isEqualTo(newsletterConfiguration)
    }

    @Test
    fun `saves the configuration with the sources to the database`() {
        // Given
        val newsletterConfiguration = aNewsletterConfiguration()
            .withRecipientId(checkNotNull(recipient.id))
            .withSources(aSource())
            .build()

        // When
        newsletterConfigurationDatabaseAdapter.save(newsletterConfiguration)

        // Then
        assertThat(newsletterConfigurationDatabaseAdapter.ofId(newsletterConfiguration.id))
            .usingRecursiveComparison()
            .isEqualTo(newsletterConfiguration)
    }

    @Test
    fun `throws an exception when the configuration is not found`() {
        val idWithoutConfiguration = UUID.randomUUID()

        assertThatThrownBy { newsletterConfigurationDatabaseAdapter.ofId(idWithoutConfiguration) }
            .isInstanceOf(NoSuchElementException::class.java)
            .hasMessage("Newsletter configuration $idWithoutConfiguration was not found")
    }
}

private fun NewsletterConfigurationDatabaseAdapter.save(vararg newsletterConfigurations: NewsletterConfiguration) {
    newsletterConfigurations.forEach { save(it) }
}
