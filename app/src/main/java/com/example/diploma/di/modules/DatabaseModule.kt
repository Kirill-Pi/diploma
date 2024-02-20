package com.example.diploma.di.modules

import android.content.Context
import androidx.room.Room
import com.example.diploma.data.MainRepository
import com.example.diploma.data.dao.LaunchDao
import com.example.diploma.data.dao.SpaceCraftsDao
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
    @Provides
    @Singleton
    fun provideRepository(launchDao: LaunchDao, spaceCraftsDao: SpaceCraftsDao) = MainRepository(launchDao, spaceCraftsDao)
}