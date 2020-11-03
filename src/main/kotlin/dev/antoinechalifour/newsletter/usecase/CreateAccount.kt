package dev.antoinechalifour.newsletter.usecase

import dev.antoinechalifour.newsletter.application.UserDetails
import dev.antoinechalifour.newsletter.domain.Recipient
import dev.antoinechalifour.newsletter.domain.RecipientPort
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CreateAccount(val recipientPort: RecipientPort) {

    operator fun invoke(userDetails: UserDetails): Recipient {
        val recipient = try {
            recipientPort.ofEmail(userDetails.email).apply {
                email = userDetails.email
                name = userDetails.name
            }
        } catch (e: NoSuchElementException) {
            Recipient(UUID.randomUUID(), userDetails.name, userDetails.email)
        }

        recipientPort.save(recipient)

        return recipient
    }
}
