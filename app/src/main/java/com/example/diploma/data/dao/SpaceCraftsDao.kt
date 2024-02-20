package com.example.diploma.data.dao

import androidx.room.*
import com.example.diploma.data.Launch
import com.example.diploma.data.SpacecraftConfig
import kotlinx.coroutines.flow.Flow

@Dao
interface SpaceCraftsDao {
    //Запрос на всю таблицу
    @Query("SELECT * FROM cached_spacecrafts")
    fun getCachedSC(): Flow<MutableList<SpacecraftConfig>>

    @Query("SELECT * FROM cached_spacecrafts WHERE isInFavorites = true")
    fun getFavorites(): Flow<MutableList<SpacecraftConfig>>

    @Query("SELECT * FROM cached_spacecrafts WHERE wasSeen = true")
    fun getLastSeen(): Flow<MutableList<SpacecraftConfig>>

    //Кладём списком в БД, в случае конфликта перезаписываем
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<SpacecraftConfig>)

    @Query("DELETE FROM cached_spacecrafts")
    fun deleteAll ()

    @Update
    fun updateSpaceCraft(spaceCraft: SpacecraftConfig)
}