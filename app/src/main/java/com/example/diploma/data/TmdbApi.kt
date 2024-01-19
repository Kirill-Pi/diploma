package com.example.diploma.data

import retrofit2.Call
import retrofit2.http.GET

interface TmdbApi {
    @GET("/2.2.0/config/spacecraft/")
    fun getSpacecraftConfig(): Call<TmdbSpacecraftConfig>


}