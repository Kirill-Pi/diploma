package com.example.diploma.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.diploma.data.Favorites
import com.example.diploma.data.RecentlySeen
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentlySeenDao {
    //Запрос на всю таблицу
    @Query("SELECT * FROM recently_seen")
    fun getCachedRecentlySeen(): Flow<MutableList<RecentlySeen>>


    //Кладём списком в БД, в случае конфликта перезаписываем
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<RecentlySeen>)

    @Query("DELETE FROM recently_seen")
    fun deleteAll ()

    @Insert
    fun updateRecentlySeen (spaceCraft: RecentlySeen)

    }