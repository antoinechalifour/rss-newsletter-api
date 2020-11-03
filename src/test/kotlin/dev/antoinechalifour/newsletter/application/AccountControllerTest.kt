package dev.antoinechalifour.newsletter.application

import com.nhaarman.mockitokotlin2.whenever
import dev.antoinechalifour.newsletter.RecipientTestBuilder.Companion.aRecipient
import dev.antoinechalifour.newsletter.asUserDetails
import dev.antoinechalifour.newsletter.bearerToken
import dev.antoinechalifour.newsletter.usecase.SynchronizeAccount
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
internal class AccountControllerTest : ApiIntegrationTest() {

    private lateinit var userDetails: UserDetails
    private val authorizedToken = "my-token"

    @MockBean
    private lateinit var synchronizeAccount: SynchronizeAccount

    @BeforeEach
    fun setup() {
        userDetails = authorizeForToken(authorizedToken).asUserDetails()

        whenever(authenticationService.userDetails()).thenReturn(userDetails)
    }

    @Test
    fun `should not be accessible without an id token`() {
        checkAuthentication { post("/api/v1/account") }
    }

    @Test
    fun `should return a 201 with the account information`() {
        // Given
        val theRecipient = aRecipient().build()
        whenever(synchronizeAccount(userDetails)).thenReturn(theRecipient)

        // When Then
        mockMvc.post("/api/v1/account") { bearerToken(authorizedToken) }
            .andExpect {
                status { isCreated }
                jsonPath("$.id", equalTo(theRecipient.id.toString()))
                jsonPath("$.email", equalTo(theRecipient.email))
                jsonPath("$.name", equalTo(theRecipient.name))
                content { contentType(MediaType.APPLICATION_JSON) }
            }
    }
}
