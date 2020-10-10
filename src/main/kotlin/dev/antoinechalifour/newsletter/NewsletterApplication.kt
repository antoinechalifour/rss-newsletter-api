package dev.antoinechalifour.newsletter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NewsletterApplication

fun main(args: Array<String>) {
	runApplication<NewsletterApplication>(*args)
}
