package dev.antoinechalifour.newsletter.infrastructure

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface RssService {
    @GET
    fun getRss(@Url url: String): Call<RssFeedXml>
}
