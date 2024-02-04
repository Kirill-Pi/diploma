package com.example.diploma.di.modules

import android.content.Context
import androidx.room.Room
import com.example.diploma.data.MainRepository
import com.example.diploma.data.dao.LaunchDao
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
    @Provides
    @Singleton
    fun provideRepository(launchDao: LaunchDao) = MainRepository(launchDao)
}