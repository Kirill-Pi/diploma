package com.example.diploma.domain

import com.example.diploma.data.*
import com.example.diploma.utils.ConverterApi
import com.example.diploma.viewmodel.EventsViewModel
import com.example.diploma.viewmodel.LaunchesViewModel
import com.example.diploma.viewmodel.SpaceShipsViewModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Interactor(private val repo: MainRepository, private val retrofitService: TmdbApi, private val preferences: PreferenceProvider) {


    fun getSpaceShipFromApi(callback: SpaceShipsViewModel.ApiCallback, offset: Int, isFirst: Boolean) {
        var inUse: MutableList<Boolean> = mutableListOf()
        when (getIsInUseFromPreferences()){
            1 ->  inUse = mutableListOf(true)
            2 ->  inUse = mutableListOf(false)
            3 ->  inUse = mutableListOf(true, false)
        }
        var humanRated: MutableList<Boolean> = mutableListOf()
        when (getIsMannedFromPreferences()){

            1 ->   humanRated = mutableListOf(true)
            2 ->   humanRated = mutableListOf(false)
            3 ->   humanRated = mutableListOf(true, false)

        }

        retrofitService.getSpacecrafts(inUse,humanRated,offset).enqueue(object : Callback<TmdbSpacecraftConfig> {
            override fun onResponse(call: Call<TmdbSpacecraftConfig>, response: Response<TmdbSpacecraftConfig>) {
                //При успехе мы вызываем метод передаем onSuccess и в этот коллбэк список фильмов
                //if (isFirst) repo.cleanSpaceCraftDb()
                if (offset == 0) repo.cleanSpaceCraftDb()
                val list = ConverterApi.converterOfSpacecrafts(response.body())
                //val list = response.body()!!
                println (list)
                repo.putSpacecraftToDb(list)
                callback.onSuccess(list)
            }

            override fun onFailure(call: Call<TmdbSpacecraftConfig>, t: Throwable) {
                //В случае провала вызываем другой метод коллбека
                callback.onFailure()
            }
        })
    }

    fun getSpacecraftsFromDB(): Flow<MutableList<SpacecraftConfig>> = repo.getAllSpaceCraftsFromDB()

    fun getFavoritesFromDB(): Flow<MutableList<SpacecraftConfig>> = repo.getFavoritesFromDB()

    fun getLastSeenFromDB(): Flow<MutableList<SpacecraftConfig>> = repo.getLastSeenFromDB()

    fun updateSpacecraftDB (spaceCraft: SpacecraftConfig) = repo.updateSpacecraftDB(spaceCraft)

    fun getEventsFromApi(callback: EventsViewModel.ApiCallback, offset: Int) {
        retrofitService.getEvents(offset).enqueue(object : Callback<TmdbEvents> {
            override fun onResponse(call: Call<TmdbEvents>, response: Response<TmdbEvents>) {
                //При успехе мы вызываем метод передаем onSuccess и в этот коллбэк список фильмов

                val list = ConverterApi.converterOfEvents(response.body())
                //val list = response.body()!!
                println (list)
                repo.putEventToDb(list)

              callback.onSuccess(list)
            }

            override fun onFailure(call: Call<TmdbEvents>, t: Throwable) {
                //В случае провала вызываем другой метод коллбека
                callback.onFailure()
            }
        })
    }

    fun getEventsFromDB(): Flow<MutableList<Events>> = repo.getAllEventsFromDB()

    fun getLaunchesFromApi(callback: LaunchesViewModel.ApiCallback, date: String, offset: Int) {
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

    //Метод для сохранения настроек
    fun saveCountryToPreferences(country: String) {
        preferences.saveCountry(country)
    }
    fun saveIsManned(isManned:Int) {
        preferences.saveIsManned(isManned)
    }
    fun saveIsInUse(isInUse:Int) {
        preferences.saveIsInUse(isInUse)
    }




    //Метод для получения настроек
    fun getCountryFromPreferences() = preferences.getCountry()

    fun getIsMannedFromPreferences() = preferences.getIsManned()

    fun getIsInUseFromPreferences() = preferences.getIsInUse()
}