package dev.antoinechalifour.newsletter.asserts

import dev.antoinechalifour.newsletter.domain.Recipient
import org.assertj.core.api.AbstractAssert
import org.simplejavamail.api.email.Email

class EmailAssert(actual: Email) : AbstractAssert<EmailAssert, Email>(actual, EmailAssert::class.java) {
    companion object {
        fun assertThat(actual: Email) = EmailAssert(actual)
    }

    fun hasBeenSentBy(sender: Recipient) = apply {
        actual.fromRecipient
            ?.let { verifyRecipient(it, sender) }
            ?: failWithMessage("Expected sender $sender but was null")
    }

    fun hasOnlyRecipient(recipient: Recipient) = apply {
        val numberOfRecipients = actual.recipients.size

        if (numberOfRecipients > 1) failWithMessage("Expected only one recipient, got $numberOfRecipients")

        verifyRecipient(actual.recipients[0], recipient)
    }

    fun hasSubject(subject: String) = apply {
        if (actual.subject != subject)
            failWithMessage("Expected subject <%s> but was <%s>", subject, actual.subject)
    }

    fun hasContent(content: String) = apply {
        if (actual.htmlText != content)
            failWithMessage("Expected content <%s> but was <%s>", content, actual.htmlText)
    }

    private fun verifyRecipient(
        actualRecipient: org.simplejavamail.api.email.Recipient,
        expectedRecipient: Recipient
    ) {
        if (actualRecipient.address != expectedRecipient.email)
            failWithMessage(
                "Expected recipient email to be <%s> but was <%s>",
                expectedRecipient.email,
                actualRecipient.address
            )

        if (actualRecipient.name != expectedRecipient.name)
            failWithMessage(
                "Expected recipient name to be <%s> but was <%s>",
                expectedRecipient.name,
                actualRecipient.name
            )
    }
}
