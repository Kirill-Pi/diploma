package com.example.diploma.di.modules

import android.content.Context
import androidx.room.Room
import com.example.diploma.data.MainRepository
import com.example.diploma.data.dao.*
import com.example.diploma.db.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideLaunchDao(context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "launch_db"
        ).fallbackToDestructiveMigration()
            .build().launchDao()
    @Singleton
    @Provides
    fun provideSpaceCraftDao (context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "spacecrafts_db"
        ).fallbackToDestructiveMigration()
            .build().spaceCraftDao()

    @Singleton
    @Provides
    fun provideEventDao (context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "events_db"
        ).fallbackToDestructiveMigration()
            .build().eventDao()
    @Singleton
    @Provides
    fun provideFavoritesDao (context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "favorites_db"
        ).fallbackToDestructiveMigration()
            .build().favoritesDao()
    @Singleton
    @Provides
    fun provideRecentlySeenDao (context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "recently_seen_db"
        ).fallbackToDestructiveMigration()
            .build().recentlySeenDao()

    @Provides
    @Singleton
    fun provideRepository(launchDao: LaunchDao, spaceCraftsDao: SpaceCraftsDao, eventDao: EventDao, favoritesDao: FavoritesDao, recentlySeenDao: RecentlySeenDao) = MainRepository(launchDao, spaceCraftsDao, eventDao, favoritesDao, recentlySeenDao)
}