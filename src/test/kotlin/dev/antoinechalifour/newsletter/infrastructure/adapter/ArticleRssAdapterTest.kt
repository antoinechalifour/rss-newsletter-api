package dev.antoinechalifour.newsletter.infrastructure.adapter

import dev.antoinechalifour.newsletter.asTestResourceFileContent
import dev.antoinechalifour.newsletter.domain.Article
import dev.antoinechalifour.newsletter.domain.Source
import dev.antoinechalifour.newsletter.infrastructure.http.rss.RssService
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.net.HttpURLConnection
import java.time.LocalDateTime
import java.util.UUID

internal class ArticleRssAdapterTest {
    private lateinit var rssService: RssService
    private var mockWebServer = MockWebServer()

    @BeforeEach
    fun setup() {
        mockWebServer.start()

        rssService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
            .create(RssService::class.java)
    }

    @AfterEach
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `returns the articles from the RSS feed`() {
        // Given
        val articlesRssAdapter =
            ArticleRssAdapter(rssService)
        mockWebServer.enqueue(aValidRssFeed())

        // When
        val articles = articlesRssAdapter.ofSource(aSource())

        // Then
        assertThat(articles).usingRecursiveComparison()
            .isEqualTo(theArticles())
    }

    private fun aValidRssFeed() = MockResponse()
        .setResponseCode(HttpURLConnection.HTTP_OK)
        .setBody("/test-http/rss-feed.xml".asTestResourceFileContent())

    private fun theArticles() = listOf(
        Article(
            "RSS Solutions for Restaurants",
            "http://www.feedforall.com/restaurant.htm",
            LocalDateTime.of(2004, 10, 19, 11, 9, 11)
        ),
        Article(
            "RSS Solutions for Schools and Colleges",
            "http://www.feedforall.com/schools.htm",
            LocalDateTime.of(2004, 10, 19, 11, 9, 9)
        )
    )

    private fun aSource() = Source(
        UUID.fromString("57133977-6266-4f62-9335-b9150bd453a2"),
        mockWebServer.url("/feed.xml").toString()
    )
}
