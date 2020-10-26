package dev.antoinechalifour.newsletter.infrastructure.adapter

import dev.antoinechalifour.newsletter.domain.Newsletter
import dev.antoinechalifour.newsletter.domain.NewsletterSender
import dev.antoinechalifour.newsletter.infrastructure.http.mjml.MjmlBody
import dev.antoinechalifour.newsletter.infrastructure.http.mjml.MjmlService
import org.simplejavamail.api.mailer.Mailer
import org.simplejavamail.email.EmailBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine

@Component
class NewsletterMailSender(
    @Qualifier("emailTemplateEngine") val emailTemplates: SpringTemplateEngine,
    val mailer: Mailer,
    val mjmlService: MjmlService
) : NewsletterSender {
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

        val mjml = emailTemplates.process("newsletter", context)
        val response = mjmlService.render(
            MjmlBody(
                mjml
            )
        ).execute()

        return response.body()?.html ?: throw IllegalStateException("Mjml service down")
    }
}
