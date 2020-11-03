package dev.antoinechalifour.newsletter.application

import dev.antoinechalifour.newsletter.domain.Recipient
import java.util.UUID

class AccountResponse(id: UUID, val name: String, val email: String) {
    val id = id.toString()

    companion object {
        fun of(recipient: Recipient) = AccountResponse(
            recipient.id,
            recipient.name,
            recipient.email
        )
    }
}
