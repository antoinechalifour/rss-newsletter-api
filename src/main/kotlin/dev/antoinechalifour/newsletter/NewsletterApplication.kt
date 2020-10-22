package dev.antoinechalifour.newsletter

import dev.antoinechalifour.newsletter.domain.Recipient
import dev.antoinechalifour.newsletter.infrastructure.MjmlService
import dev.antoinechalifour.newsletter.infrastructure.RssService
import okhttp3.Credentials
import okhttp3.OkHttpClient
import org.simplejavamail.springsupport.SimpleJavaMailSpringSupport
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableScheduling
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect
import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun mjmlService(
        @Value("\${mjml.user}") mjmlUser: String,
        @Value("\${mjml.password}") mjmlPassword: String
    ) =
        Retrofit.Builder()
            .baseUrl("https://api.mjml.io/")
            .client(okHttpClient(mjmlUser, mjmlPassword))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MjmlService::class.java)

    @Bean
    fun recipient(
        @Value("\${newsletter.recipient.name}") name: String,
        @Value("\${newsletter.recipient.email}") email: String
    ) = Recipient(name, email)

    @Bean
    fun clock() = Clock.system(ZoneId.of("Europe/Paris"))

    @Bean
    fun emailTemplateEngine() = SpringTemplateEngine().apply {
        addDialect(Java8TimeDialect())
        setTemplateResolver(emailTemplateResolver())
    }

    private fun emailTemplateResolver() = ClassLoaderTemplateResolver().apply {
        prefix = "emails/"
        suffix = ".html"
        templateMode = TemplateMode.HTML
        characterEncoding = "UTF-8"
    }

    private fun okHttpClient(mjmlUser: String, mjmlPassword: String) = OkHttpClient().newBuilder()
        .addInterceptor {
            val newRequest = it.request().newBuilder()
                .header("Authorization", Credentials.basic(mjmlUser, mjmlPassword))
                .build()

            it.proceed(newRequest)
        }.build()
}

fun main(args: Array<String>) {
    runApplication<NewsletterApplication>(*args)
}
