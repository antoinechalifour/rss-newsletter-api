package dev.antoinechalifour.newsletter.infrastructure.database

import dev.antoinechalifour.newsletter.domain.Recipient
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "recipient")
open class RecipientDatabase(
    @Id
    open var id: UUID? = null,
    open var name: String? = null,
    open var email: String? = null
) {
    fun toRecipient() = Recipient(
        checkNotNull(id),
        checkNotNull(name),
        checkNotNull(email)
    )

    companion object {
        fun of(recipient: Recipient) = RecipientDatabase(
            recipient.id,
            recipient.name,
            recipient.email
        )
    }
}
