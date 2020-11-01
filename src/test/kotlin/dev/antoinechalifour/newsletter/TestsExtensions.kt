package dev.antoinechalifour.newsletter

import okio.Buffer
import org.springframework.test.web.servlet.MockHttpServletRequestDsl
import org.springframework.util.Base64Utils

fun String.asTestResourceFileContent() = NewsletterApplicationTests::class.java.getResource(this).readText()

private fun String.encode() = "Basic " + Base64Utils.encodeToString(this.toByteArray())

fun MockHttpServletRequestDsl.bearerToken(fakeToken: String) =
    header("Authorization", "Bearer $fakeToken")

fun Buffer.string() = this.readByteString().utf8()
