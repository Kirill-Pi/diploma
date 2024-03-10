package com.example.diploma.data

import com.example.diploma.data.dao.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.EmptyCoroutineContext

class MainRepository (private val launchDao: LaunchDao, private val spaceCraftDao: SpaceCraftsDao, private val eventDao: EventDao, private val favoritesDao: FavoritesDao, private val recentlySeenDao: RecentlySeenDao){

    fun putToDb(news: List<Launch>) {
        CoroutineScope(EmptyCoroutineContext).launch {
            launchDao.insertAll(news)
        }
    }

    fun cleanLaunchesDb() {
        CoroutineScope(EmptyCoroutineContext).launch {
            launchDao.deleteAll()
        }
    }

    fun getAllLaunchesFromDB(): Flow<MutableList<Launch>> {
        return launchDao.getCachedNews()
    }

    fun putSpacecraftToDb(spaseCrafts: List<SpacecraftConfig>) {
        CoroutineScope(EmptyCoroutineContext).launch {
            //delay(200)
            spaceCraftDao.insertAll(spaseCrafts)
        }
    }

    fun cleanSpaceCraftDb() {
        CoroutineScope(EmptyCoroutineContext).launch {
           spaceCraftDao.deleteAll()
        }
    }

    fun getAllSpaceCraftsFromDB(): Flow<MutableList<SpacecraftConfig>> {
        return spaceCraftDao.getCachedSC()
    }

    fun getFavoritesFromDB(): Flow<MutableList<SpacecraftConfig>> {
        return spaceCraftDao.getFavorites()
    }

    fun getLastSeenFromDB(): Flow<MutableList<SpacecraftConfig>> {
        return spaceCraftDao.getLastSeen()
    }

    fun updateSpacecraftDB ( spaceCraft: SpacecraftConfig){
        CoroutineScope(EmptyCoroutineContext).launch {
            spaceCraftDao.updateSpaceCraft(spaceCraft)
        }
    }

    fun putEventToDb(events: List<Events>) {
        CoroutineScope(EmptyCoroutineContext).launch {
            eventDao.insertAll(events)
        }
    }

    fun cleanEventsDb() {
        CoroutineScope(EmptyCoroutineContext).launch {
            eventDao.deleteAll()
        }
    }

    fun getAllEventsFromDB(): Flow<MutableList<Events>> {
        return eventDao.getCachedEvents()
    }

    fun cleanFavoritesDb() {
        CoroutineScope(EmptyCoroutineContext).launch {
            favoritesDao.deleteAll()
        }
    }

    fun getAllFavoritesFromDB(): Flow<MutableList<Favorites>> {
        return favoritesDao.getCachedFavorites()
    }

    fun updateFavoritesDB ( spaceCraft: Favorites){
        CoroutineScope(EmptyCoroutineContext).launch {
            favoritesDao.updateFavorites(spaceCraft)
        }

    }

    fun deleteItemFromFavoritesDb(query: String) {
        CoroutineScope(EmptyCoroutineContext).launch {
            favoritesDao.deleteItem(query)
        }
    }

    fun getFavoriteByNameFromDB(query: String): Int {
        var result = 0
       val deferredResult =  CoroutineScope(EmptyCoroutineContext).async {
           return@async favoritesDao.selectByName(query)
       }
        runBlocking {
            result = deferredResult.await()
        }
        return result
    }

    fun cleanRecentlySeenDb() {
        CoroutineScope(EmptyCoroutineContext).launch {
            recentlySeenDao.deleteAll()
        }
    }

    fun getAllRecentlySeenFromDB(): Flow<MutableList<RecentlySeen>> {
        return recentlySeenDao.getCachedRecentlySeen()
    }

    fun updateRecentlySeenDB ( spaceCraft: RecentlySeen){
        CoroutineScope(EmptyCoroutineContext).launch {
            recentlySeenDao.updateRecentlySeen(spaceCraft)
        }
    }

}