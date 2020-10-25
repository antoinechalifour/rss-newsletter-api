package dev.antoinechalifour.newsletter

import dev.antoinechalifour.newsletter.domain.Recipient

class RecipientTestBuilder {
    var name = "John Doe"
    var email = "john.doe@email.com"

    fun build() = Recipient(name, email)
}
