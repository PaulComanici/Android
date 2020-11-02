package com.example.movieapp.data

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object ItemApi {
    private const val URL = "http://192.168.100.7:3000/"

    interface Service {
        @GET("/movie")
        suspend fun find(): List<Movie>

        @GET("/movie/{id}")
        suspend fun read(@Path("id") itemId: String): Movie;

        @Headers("Content-Type: application/json")
        @POST("/movie")
        suspend fun create(@Body item: Movie): Movie

        @Headers("Content-Type: application/json")
        @PUT("/movie/{id}")
        suspend fun update(@Path("id") itemId: String, @Body item: Movie): Movie
    }

    private val client: OkHttpClient = OkHttpClient.Builder().build()

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    val service: Service = retrofit.create(Service::class.java)
}