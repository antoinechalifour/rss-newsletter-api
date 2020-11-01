package dev.antoinechalifour.newsletter

import com.nhaarman.mockitokotlin2.whenever
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder

abstract class IntegrationTest {

    @MockBean
    protected lateinit var jwtDecoder: JwtDecoder

    @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private lateinit var iss: String
    protected fun authorizeForToken(fakeToken: String) {
        val jwt = Jwt.withTokenValue(fakeToken)
            .header("type", "JWT")
            .claim("iss", iss)
            .claim("sub", "117235481458145255036")
            .claim("email", "john.doe@gmail.com")
            .claim("name", "John Doe")
            .build()

        whenever(jwtDecoder.decode(fakeToken)).thenReturn(jwt)
    }
}
