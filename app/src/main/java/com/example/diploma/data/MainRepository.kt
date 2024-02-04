package com.example.diploma.data

import com.example.diploma.data.dao.LaunchDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

class MainRepository (private val launchDao: LaunchDao){

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
}