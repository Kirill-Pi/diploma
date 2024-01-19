package com.example.diploma.domain

import com.example.diploma.data.MainRepository
import com.example.diploma.data.TmdbApi
import com.example.diploma.data.TmdbSpacecraftConfig
import com.example.diploma.viewmodel.SpaceShipsViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Interactor(private val repo: MainRepository, private val retrofitService: TmdbApi) {


    fun getSpaceShipFromApi(callback: SpaceShipsViewModel.ApiCallback) {
        retrofitService.getSpacecraftConfig().enqueue(object : Callback<TmdbSpacecraftConfig> {
            override fun onResponse(call: Call<TmdbSpacecraftConfig>, response: Response<TmdbSpacecraftConfig>) {
                //При успехе мы вызываем метод передаем onSuccess и в этот коллбэк список фильмов

                //val list = ConverterApi.converterOfNews(response.body())
                val list = response.body()!!
                println (list)

                callback.onSuccess(list)
            }

            override fun onFailure(call: Call<TmdbSpacecraftConfig>, t: Throwable) {
                //В случае провала вызываем другой метод коллбека
                callback.onFailure()
            }
        })
    }
}