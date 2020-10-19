package dev.antoinechalifour.newsletter

import dev.antoinechalifour.newsletter.domain.Recipient
import dev.antoinechalifour.newsletter.infrastructure.RssService
import org.simplejavamail.springsupport.SimpleJavaMailSpringSupport
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableScheduling
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.time.Clock
import java.time.ZoneId

@SpringBootApplication
@EnableScheduling
@Import(SimpleJavaMailSpringSupport::class)
class NewsletterApplication {
    private val ignoredBaseUrl = "http://localhost/"

    @Bean
    fun rssService() = Retrofit.Builder()
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .baseUrl(ignoredBaseUrl)
        .build()
        .create(RssService::class.java)

    @Bean
    fun recipient(
        @Value("\${newsletter.recipient.name}") name: String,
        @Value("\${newsletter.recipient.email}") email: String
    ) = Recipient(name, email)

    @Bean
    fun clock() = Clock.system(ZoneId.of("Europe/Paris"))
}

fun main(args: Array<String>) {
    runApplication<NewsletterApplication>(*args)
}
