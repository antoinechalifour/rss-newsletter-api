package dev.antoinechalifour.newsletter

import com.nhaarman.mockitokotlin2.whenever
import dev.antoinechalifour.newsletter.RecipientTestBuilder.Companion.aRecipient
import dev.antoinechalifour.newsletter.application.AuthenticationService
import dev.antoinechalifour.newsletter.domain.Recipient
import dev.antoinechalifour.newsletter.domain.RecipientPort
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder

abstract class IntegrationTest {

    @MockBean
    protected lateinit var authenticationService: AuthenticationService

    @MockBean
    protected lateinit var jwtDecoder: JwtDecoder

    @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private lateinit var iss: String

    protected fun authorizeForToken(fakeToken: String): Recipient {
        val recipient = aRecipient().build()
        val jwt = Jwt.withTokenValue(fakeToken)
            .header("type", "JWT")
            .claim("iss", iss)
            .claim("sub", "117235481458145255036")
            .claim("email", recipient.email)
            .claim("name", recipient.name)
            .build()

        whenever(jwtDecoder.decode(fakeToken)).thenReturn(jwt)
        whenever(authenticationService.currentUser()).thenReturn(recipient)

        return recipient
    }
}
