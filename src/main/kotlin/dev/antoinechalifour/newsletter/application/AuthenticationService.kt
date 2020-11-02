package dev.antoinechalifour.newsletter.application

import dev.antoinechalifour.newsletter.domain.Recipient
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class AuthenticationService {
    fun currentUser(): Recipient {
        val jwt = SecurityContextHolder.getContext().authentication.principal as Jwt
        val name = checkNotNull(jwt.claims["name"]) as String
        val email = checkNotNull(jwt.claims["email"]) as String

        return Recipient(UUID.randomUUID(), name, email)
    }

    fun userDetails() = principal().run {
        UserDetails(checkNotNull(claims["name"]) as String, checkNotNull(claims["email"]) as String)
    }

    private fun principal() = SecurityContextHolder.getContext().authentication.principal as Jwt
}
