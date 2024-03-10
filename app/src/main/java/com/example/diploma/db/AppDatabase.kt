package com.example.diploma.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.diploma.data.*
import com.example.diploma.data.dao.*


@Database(entities = [Launch::class, SpacecraftConfig::class, Events::class, Favorites::class, RecentlySeen::class], version = 15, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {
    abstract fun launchDao(): LaunchDao
    abstract fun spaceCraftDao(): SpaceCraftsDao
    abstract fun eventDao(): EventDao
    abstract fun favoritesDao(): FavoritesDao
    abstract fun recentlySeenDao(): RecentlySeenDao
}