package com.example.diploma.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.diploma.data.Events
import com.example.diploma.data.Launch
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    //Запрос на всю таблицу
    @Query("SELECT * FROM cached_events")
    fun getCachedEvents(): Flow<MutableList<Events>>

    //Кладём списком в БД, в случае конфликта перезаписываем
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Events>)

    @Query("DELETE FROM cached_events")
    fun deleteAll ()
}