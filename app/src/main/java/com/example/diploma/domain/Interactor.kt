package com.example.diploma.domain

import com.example.diploma.data.*
import com.example.diploma.utils.ConverterApi
import com.example.diploma.viewmodel.EventsViewModel
import com.example.diploma.viewmodel.SpaceShipsViewModel
import kotlinx.coroutines.flow.Flow
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

//    fun getEventsFromApi(callback: EventsViewModel.ApiCallback) {
//        retrofitService.getEvents(30,1,2024).enqueue(object : Callback<TmdbEvents> {
//            override fun onResponse(call: Call<TmdbEvents>, response: Response<TmdbEvents>) {
//                //При успехе мы вызываем метод передаем onSuccess и в этот коллбэк список фильмов
//
//                //val list = ConverterApi.converterOfNews(response.body())
//                val list = response.body()!!
//                println (list)
//
//              //  callback.onSuccess(list)
//            }
//
//            override fun onFailure(call: Call<TmdbEvents>, t: Throwable) {
//                //В случае провала вызываем другой метод коллбека
//                callback.onFailure()
//            }
//        })
//    }

    fun getLaunchesFromApi(callback: EventsViewModel.ApiCallback, date: String, offset: Int) {
        retrofitService.getLaunches(date, offset).enqueue(object : Callback<TmdbLaunch> {
            override fun onResponse(call: Call<TmdbLaunch>, response: Response<TmdbLaunch>) {

                val list = ConverterApi.converterOfLaunches(response.body())
                println (list)
                repo.putToDb(list)
                callback.onSuccess(list)
            }

            override fun onFailure(call: Call<TmdbLaunch>, t: Throwable) {
                //В случае провала вызываем другой метод коллбека
                callback.onFailure()
            }
        })
    }

    fun getLaunchesFromDB(): Flow<MutableList<Launch>> = repo.getAllLaunchesFromDB()

    fun cleanDb (){
        repo.cleanLaunchesDb()
    }
}