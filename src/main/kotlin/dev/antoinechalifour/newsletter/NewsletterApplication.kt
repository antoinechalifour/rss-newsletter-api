package dev.antoinechalifour.newsletter

import dev.antoinechalifour.newsletter.infrastructure.RssService
import org.simplejavamail.api.mailer.Mailer
import org.simplejavamail.mailer.MailerBuilder
import org.simplejavamail.springsupport.SimpleJavaMailSpringSupport
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

@SpringBootApplication
@Import(SimpleJavaMailSpringSupport::class)
class NewsletterApplication {
    private val ignoredBaseUrl = "http://localhost/"

    @Bean
    fun rssService() = Retrofit.Builder()
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .baseUrl(ignoredBaseUrl)
        .build()
        .create(RssService::class.java)

}

fun main(args: Array<String>) {
    runApplication<NewsletterApplication>(*args)
}
