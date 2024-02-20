package com.example.diploma.data

import com.example.diploma.data.dao.LaunchDao
import com.example.diploma.data.dao.SpaceCraftsDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

class MainRepository (private val launchDao: LaunchDao, private val spaceCraftDao: SpaceCraftsDao){

    fun putToDb(news: List<Launch>) {

        CoroutineScope(EmptyCoroutineContext).launch {
            //delay(200)
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


}