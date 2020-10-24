package dev.antoinechalifour.newsletter.infrastructure.database

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface NewsletterConfigurationRepository : JpaRepository<NewsletterConfigurationDatabase, UUID>
