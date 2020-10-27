package dev.antoinechalifour.newsletter.asserts

import dev.antoinechalifour.newsletter.domain.NewsletterConfiguration
import org.assertj.core.api.AbstractAssert

class NewsletterConfigurationAssert(actual: NewsletterConfiguration) :
    AbstractAssert<NewsletterConfigurationAssert, NewsletterConfiguration>(
        actual,
        NewsletterConfigurationAssert::class.java
    ) {

    companion object {
        fun assertThat(actual: NewsletterConfiguration) = NewsletterConfigurationAssert(actual)
    }

    fun hasSourceMatchingUrl(url: String) = apply {
        actual.sources.find { it.url == url }
            ?: failWithMessage("Expected newsletter configuration to have a source matching url <%s> but got none", url)
    }

    fun hasSourceWithName(name: String) = apply {
        actual.sources.find { it.name == name }
            ?: failWithMessage("Expected newsletter configuration to have a source with name <%s> but got none", name)
    }
}
