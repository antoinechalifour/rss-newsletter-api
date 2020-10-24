package dev.antoinechalifour.newsletter.infrastructure.database

import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "newsletter_configuration")
open class NewsletterConfigurationDatabase(
    @Id
    open var id: UUID? = null
)
