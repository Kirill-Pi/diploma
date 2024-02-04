package com.example.diploma.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {
    @GET("/2.2.0/config/spacecraft/")
    fun getSpacecraftConfig(): Call<TmdbSpacecraftConfig>

    @GET("/2.2.0/event/upcoming/")
    fun getEvents(
        @Query("day") day: Int,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Call<TmdbEvents>

    @GET("/2.2.0/launch/")
    fun getLaunches(
       /* @Query("day") day: Int,
        @Query("month") month: Int,
        @Query("year") year: Int*/
        @Query("net__gte") query: String,
        @Query("offset") offset: Int,
    ): Call<TmdbLaunch>

}