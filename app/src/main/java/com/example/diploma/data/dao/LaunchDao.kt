package com.example.diploma.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.diploma.data.Launch

import kotlinx.coroutines.flow.Flow

@Dao
interface LaunchDao {
    //Запрос на всю таблицу
    @Query("SELECT * FROM cached_launches")
    fun getCachedNews(): Flow<MutableList<Launch>>

    //Кладём списком в БД, в случае конфликта перезаписываем
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Launch>)

    @Query("DELETE FROM cached_launches")
    fun deleteAll ()
}