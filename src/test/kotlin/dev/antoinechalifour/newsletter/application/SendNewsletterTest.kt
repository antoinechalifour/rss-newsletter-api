package dev.antoinechalifour.newsletter.application

import com.nhaarman.mockitokotlin2.verify
import dev.antoinechalifour.newsletter.basicAuth
import dev.antoinechalifour.newsletter.usecase.SendNewsletter
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
internal class SendNewsletterTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var sendNewsletter: SendNewsletter

    @Test
    fun `should not be accessible without authentication`() {
        mockMvc.post("/newsletter")
            .andExpect {
                status { isUnauthorized }
            }
    }

    @Test
    fun `should send the newsletter`() {
        mockMvc.post("/newsletter") { basicAuth("admin", "passwd") }
            .andExpect {
                status { isOk }
                content { string("Newsletter sent!") }
            }

        verify(sendNewsletter).invoke()
    }
}
