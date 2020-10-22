package dev.antoinechalifour.newsletter.infrastructure

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MjmlService {
    @POST("/v1/render")
    fun render(@Body body: MjmlBody): Call<MjmlResponse>
}
