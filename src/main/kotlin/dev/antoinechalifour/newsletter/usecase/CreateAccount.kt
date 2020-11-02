package dev.antoinechalifour.newsletter.usecase

import dev.antoinechalifour.newsletter.application.UserDetails
import dev.antoinechalifour.newsletter.domain.Recipient
import dev.antoinechalifour.newsletter.domain.RecipientPort
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CreateAccount(val recipientPort: RecipientPort) {

    operator fun invoke(userDetails: UserDetails) =
        Recipient(UUID.randomUUID(), userDetails.name, userDetails.email)
            .also { recipientPort.save(it) }
}
