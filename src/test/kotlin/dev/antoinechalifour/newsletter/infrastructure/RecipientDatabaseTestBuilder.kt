package dev.antoinechalifour.newsletter.infrastructure

import dev.antoinechalifour.newsletter.infrastructure.database.RecipientDatabase
import java.util.UUID

class RecipientDatabaseTestBuilder {
    private var id = UUID.randomUUID()
    private var name = "John Doe"
    private var email = "john.doe@gmail.com"

    fun build() = RecipientDatabase(id, name, email)

}
