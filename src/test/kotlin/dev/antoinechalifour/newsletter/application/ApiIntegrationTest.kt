package dev.antoinechalifour.newsletter.application

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl

abstract class ApiIntegrationTest {
    @Autowired
    protected lateinit var mockMvc: MockMvc

    fun checkAuthentication(operation: MockMvc.() -> ResultActionsDsl) = operation(mockMvc).andExpect {
        status { isUnauthorized }
    }

}
