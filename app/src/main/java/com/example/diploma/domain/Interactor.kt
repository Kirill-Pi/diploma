package com.example.diploma.domain

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.example.diploma.data.*
import com.example.diploma.utils.ConverterApi
import com.example.diploma.viewmodel.EventsViewModel
import com.example.diploma.viewmodel.LaunchesViewModel
import com.example.diploma.viewmodel.SpaceShipsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Interactor(private val repo: MainRepository, private val retrofitService: TmdbApi, private val preferences: PreferenceProvider) {

    private lateinit var scope: CoroutineScope

    fun getSpaceShipFromApi(
        callback: SpaceShipsViewModel.ApiCallback,
        query: String,
        offset: Int,
        isFirst: Boolean
    ) {
        var inUse: MutableList<Boolean> = mutableListOf()
        when (getIsInUseFromPreferences()) {
            1 -> inUse = mutableListOf(true)
            2 -> inUse = mutableListOf(false)
            3 -> inUse = mutableListOf(true, false)
        }
        var humanRated: MutableList<Boolean> = mutableListOf()
        when (getIsMannedFromPreferences()) {

            1 -> humanRated = mutableListOf(true)
            2 -> humanRated = mutableListOf(false)
            3 -> humanRated = mutableListOf(true, false)
        }
        retrofitService.getSpacecrafts(inUse, humanRated, query, offset)
            .enqueue(object : Callback<TmdbSpacecraftConfig> {
                override fun onResponse(
                    call: Call<TmdbSpacecraftConfig>,
                    response: Response<TmdbSpacecraftConfig>
                ) {
                    //При успехе мы вызываем метод передаем onSuccess и в этот коллбэк список
                    if (offset == 0) repo.cleanSpaceCraftDb()
                    val list = ConverterApi.converterOfSpacecrafts(response.body())
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


    fun getLastSeenFromDB(): Flow<MutableList<SpacecraftConfig>> = repo.getLastSeenFromDB()

    fun updateSpacecraftDB(spaceCraft: SpacecraftConfig) = repo.updateSpacecraftDB(spaceCraft)

    fun getEventsFromApi(callback: EventsViewModel.ApiCallback, query: String, offset: Int) {
        retrofitService.getEvents(query, offset).enqueue(object : Callback<TmdbEvents> {
            override fun onResponse(call: Call<TmdbEvents>, response: Response<TmdbEvents>) {
                //При успехе мы вызываем метод передаем onSuccess и в этот коллбэк список
                if (offset == 0) repo.cleanEventsDb()
                val list = ConverterApi.converterOfEvents(response.body())
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

    fun getLaunchesFromApi(
        callback: LaunchesViewModel.ApiCallback,
        date: String,
        query: String,
        offset: Int
    ) {
        retrofitService.getLaunches(date, query, offset).enqueue(object : Callback<TmdbLaunch> {
            override fun onResponse(call: Call<TmdbLaunch>, response: Response<TmdbLaunch>) {
                if (offset == 0) cleanDb()
                val list = ConverterApi.converterOfLaunches(response.body())
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

    fun cleanDb() {
        repo.cleanLaunchesDb()
    }

    //Метод для сохранения настроек
    fun saveCountryToPreferences(country: String) {
        preferences.saveCountry(country)
    }

    fun saveIsManned(isManned: Int) {
        preferences.saveIsManned(isManned)
    }

    fun saveIsInUse(isInUse: Int) {
        preferences.saveIsInUse(isInUse)
    }


    //Метод для получения настроек
    fun getCountryFromPreferences() = preferences.getCountry()

    fun getIsMannedFromPreferences() = preferences.getIsManned()

    fun getIsInUseFromPreferences() = preferences.getIsInUse()

    fun getFavoritesFromDB(): Flow<MutableList<Favorites>> = repo.getAllFavoritesFromDB()


    fun updateFavoritesDB(spaceCraft: SpacecraftConfig) {
        var favoriteSpacecraft = Favorites(
            agency = spaceCraft.agency,
            capability = spaceCraft.capability,
            crewCapacity = spaceCraft.crewCapacity,
            humanRated = spaceCraft.humanRated,
            imageUrl = spaceCraft.imageUrl,
            inUse = spaceCraft.inUse,
            maidenFlight = spaceCraft.maidenFlight,
            name = spaceCraft.name,
            countryCode = spaceCraft.countryCode
        )
        repo.updateFavoritesDB(favoriteSpacecraft)
    }

    fun deleteFavoriteItem(name: String) = repo.deleteItemFromFavoritesDb(name)

    fun checkFavoriteByName(name: String): Boolean {
        var temp = repo.getFavoriteByNameFromDB(name)
        return repo.getFavoriteByNameFromDB(name) != 0
    }


    fun getRecentlySeenFromDB(): Flow<MutableList<RecentlySeen>> = repo.getAllRecentlySeenFromDB()


    fun updateRecentlySeenDB(spaceCraft: SpacecraftConfig) {
        var recentlySeenSpacecraft = RecentlySeen(
            imageUrl = spaceCraft.imageUrl,
            name = spaceCraft.name
        )
        repo.updateRecentlySeenDB(recentlySeenSpacecraft)
    }

    fun cleanRecentlySeenDb() {
        repo.cleanRecentlySeenDb()
    }

}


