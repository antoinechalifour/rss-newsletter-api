package dev.antoinechalifour.newsletter.application

import dev.antoinechalifour.newsletter.domain.RecipientPort
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Component
class AuthenticationService(val recipientPort: RecipientPort) {

    fun currentUser() = try {
        recipientPort.ofEmail(userDetails().email)
    } catch (e: NoSuchElementException) {
        throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }

    fun userDetails() = principal().run {
        UserDetails(
            email = checkNotNull(claims["email"]) as String,
            name = checkNotNull(claims["name"]) as String
        )
    }

    private fun principal() = SecurityContextHolder.getContext().authentication.principal as Jwt
}
