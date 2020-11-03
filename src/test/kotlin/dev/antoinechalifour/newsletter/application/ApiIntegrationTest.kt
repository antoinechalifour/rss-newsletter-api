package dev.antoinechalifour.newsletter.application

import dev.antoinechalifour.newsletter.IntegrationTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl

abstract class ApiIntegrationTest : IntegrationTest() {

    @Autowired
    protected lateinit var mockMvc: MockMvc

    protected fun checkAuthentication(operation: MockMvc.() -> ResultActionsDsl) = operation(mockMvc).andExpect {
        status { isUnauthorized }
    }
}
