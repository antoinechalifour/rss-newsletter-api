package dev.antoinechalifour.newsletter

import okio.Buffer
import org.springframework.test.web.servlet.MockHttpServletRequestDsl
import org.springframework.util.Base64Utils

fun String.asTestResourceFileContent() = NewsletterApplicationTests::class.java.getResource(this).readText()

private fun String.encode() = "Basic " + Base64Utils.encodeToString(this.toByteArray())

fun MockHttpServletRequestDsl.basicAuth(username: String, password: String) =
    header("Authorization", "$username:$password".encode())

fun Buffer.string() = this.readByteString().utf8()
