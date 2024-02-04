package com.example.diploma.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.diploma.data.Launch
import com.example.diploma.data.dao.LaunchDao


@Database(entities = [Launch::class], version = 2, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {
    abstract fun launchDao(): LaunchDao
}