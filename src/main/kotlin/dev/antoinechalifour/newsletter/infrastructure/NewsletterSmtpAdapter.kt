package dev.antoinechalifour.newsletter.infrastructure

import dev.antoinechalifour.newsletter.domain.Newsletter
import dev.antoinechalifour.newsletter.domain.NewsletterPort
import org.simplejavamail.api.mailer.Mailer
import org.simplejavamail.email.EmailBuilder
import org.springframework.stereotype.Component
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine

@Component
class NewsletterSmtpAdapter(
    val mailer: Mailer,
    val emailTemplates: SpringTemplateEngine
) : NewsletterPort {
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
        val context = Context().apply {
            setVariable("recipient", newsletter.recipient.name)
            setVariable("articles", newsletter.articles)
        }

        return emailTemplates.process("newsletter", context)
    }
}
