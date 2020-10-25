package dev.antoinechalifour.newsletter.infrastructure.database

import dev.antoinechalifour.newsletter.domain.NewsletterConfiguration
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "newsletter_configuration")
open class NewsletterConfigurationDatabase(
    @Id
    open var id: UUID? = null,

    @OneToMany(
        mappedBy = "newsletterConfiguration",
        cascade = [CascadeType.ALL],
        fetch = FetchType.EAGER
    )
    open var sources: MutableList<SourceDatabase> = mutableListOf()
) {
    fun toNewsletterConfiguration() = NewsletterConfiguration(
        checkNotNull(id),
        sources.map { it.toSource() }.toMutableList()
    )

    companion object {
        fun of(newsletterConfiguration: NewsletterConfiguration): NewsletterConfigurationDatabase {
            val newsletterConfigurationDatabase = NewsletterConfigurationDatabase(
                newsletterConfiguration.id,
                mutableListOf()
            )

            newsletterConfiguration.sources.forEach {
                val sourceDatabase = SourceDatabase.of(it)
                sourceDatabase.newsletterConfiguration = newsletterConfigurationDatabase

                newsletterConfigurationDatabase.sources.add(sourceDatabase)
            }

            return newsletterConfigurationDatabase
        }
    }
}
