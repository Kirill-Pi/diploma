package com.example.diploma.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {
    @GET("/2.2.0/config/spacecraft/")
    fun getSpacecraftConfig(): Call<TmdbSpacecraftConfig>

    @GET("/2.2.0/event/")
    fun getEvents(
        //@Query("day") day: Int,
        //@Query("month") month: Int,
       // @Query("year") year: Int
        @Query("search") search: String,
        @Query("offset") offset: Int
    ): Call<TmdbEvents>

    @GET("/2.2.0/launch/")
    fun getLaunches(
        @Query("net__gte") query: String,
        @Query("search") search: String,
        @Query("offset") offset: Int,
    ): Call<TmdbLaunch>

    @GET("/2.2.0/config/spacecraft/")
    fun getSpacecrafts(
        @Query("in_use") isInUse: MutableList<Boolean>,
        @Query("human_rated") isRated: MutableList<Boolean>,
        @Query("search") search: String,
        @Query("offset") offset: Int,
        ): Call<TmdbSpacecraftConfig>


}