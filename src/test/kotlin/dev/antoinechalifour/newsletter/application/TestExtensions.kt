package dev.antoinechalifour.newsletter.application

import org.springframework.test.web.servlet.MockHttpServletRequestDsl
import org.springframework.util.Base64Utils

fun MockHttpServletRequestDsl.basicAuth(username: String, password: String) =
    header("Authorization", "$username:$password".encode())

private fun String.encode() = "Basic " + Base64Utils.encodeToString(this.toByteArray())
