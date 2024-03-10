package com.example.diploma.data.dao

import androidx.room.*
import com.example.diploma.data.Favorites
import com.example.diploma.data.SpacecraftConfig
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    //Запрос на всю таблицу
    @Query("SELECT * FROM favorites")
    fun getCachedFavorites(): Flow<MutableList<Favorites>>


    //Кладём списком в БД, в случае конфликта перезаписываем
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Favorites>)

    @Query("DELETE FROM favorites")
    fun deleteAll ()

    @Insert
    fun updateFavorites (spaceCraft: Favorites)

    @Query("DELETE FROM favorites WHERE name = :query")
    fun deleteItem (query: String)

    @Query("SELECT COUNT() FROM favorites WHERE name = :query")
    fun selectByName (query: String): Int

}