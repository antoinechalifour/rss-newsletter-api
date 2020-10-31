package dev.antoinechalifour.newsletter.domain

import java.util.UUID

data class Recipient(val id: UUID, val name: String, val email: String)
