package dev.antoinechalifour.newsletter

import dev.antoinechalifour.newsletter.domain.Recipient

class RecipientTestBuilder {

    companion object {
        fun aRecipient() = RecipientTestBuilder()

        fun aSender() = RecipientTestBuilder().apply {
            name = "Your newsletter"
            email = "newsletter.antoinechalifour@gmail.com"
        }
    }

    var name = "John Doe"
    var email = "john.doe@email.com"

    fun build() = Recipient(name, email)
}
