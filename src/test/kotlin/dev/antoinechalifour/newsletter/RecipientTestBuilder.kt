package dev.antoinechalifour.newsletter

import dev.antoinechalifour.newsletter.domain.Recipient
import java.util.UUID

class RecipientTestBuilder {

    companion object {
        fun aRecipient() = RecipientTestBuilder()

        fun aSender() = RecipientTestBuilder().apply {
            name = "Your newsletter"
            email = "newsletter.antoinechalifour@gmail.com"
        }
    }

    var id = UUID.randomUUID()
    var name = "John Doe"
    var email = "john.doe@email.com"

    fun build() = Recipient(id, name, email)
    fun withId(theId: UUID) = apply { id = theId }
    fun withEmail(theEmail: String) = apply { email = theEmail }
}
