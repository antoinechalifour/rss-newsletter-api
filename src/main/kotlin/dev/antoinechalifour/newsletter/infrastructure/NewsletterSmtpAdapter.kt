package dev.antoinechalifour.newsletter.infrastructure

import dev.antoinechalifour.newsletter.domain.Newsletter
import dev.antoinechalifour.newsletter.domain.NewsletterPort
import org.simplejavamail.api.mailer.Mailer
import org.simplejavamail.email.EmailBuilder
import org.springframework.stereotype.Component

@Component
class NewsletterSmtpAdapter(val mailer: Mailer) : NewsletterPort {
    override fun send(newsletter: Newsletter) {
        val email = EmailBuilder.startingBlank()
            .from("Your newsletter", "newsletter.antoinechalifour@gmail.com")
            .to(newsletter.recipient.name, newsletter.recipient.email)
            .withSubject("Today's newsletter")
            .withHTMLText(makeContent(newsletter))
            .buildEmail()

        mailer.sendMail(email)
    }

    private fun makeContent(newsletter: Newsletter): String {
        var content = "<ul>"
        newsletter.articles.forEach {
            content += """
                <li>
                    <a href="${it.url}">${it.title}</a>
                </li>
            """.trimIndent()
        }
        content += "</ul>"

        return content
    }
}
