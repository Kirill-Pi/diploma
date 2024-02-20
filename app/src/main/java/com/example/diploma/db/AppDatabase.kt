package com.example.diploma.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.diploma.data.Launch
import com.example.diploma.data.SpacecraftConfig
import com.example.diploma.data.dao.LaunchDao
import com.example.diploma.data.dao.SpaceCraftsDao


@Database(entities = [Launch::class, SpacecraftConfig::class], version = 9, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {
    abstract fun launchDao(): LaunchDao

    abstract fun spaceCraftDao(): SpaceCraftsDao
}