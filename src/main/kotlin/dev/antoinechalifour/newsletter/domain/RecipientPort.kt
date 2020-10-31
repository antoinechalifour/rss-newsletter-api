package dev.antoinechalifour.newsletter.domain

import java.util.UUID

interface RecipientPort {
    fun ofId(id: UUID): Recipient
    fun save(recipient: Recipient)
}
