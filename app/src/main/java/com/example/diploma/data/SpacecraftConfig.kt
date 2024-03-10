package com.example.diploma.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "cached_spacecrafts", indices = [Index(value = ["name"], unique = true)])
data class SpacecraftConfig(
    @ColumnInfo val agency: String = "",
    @ColumnInfo val capability: String = "",
    @ColumnInfo val crewCapacity: Int = 0,
    @ColumnInfo val humanRated: Boolean = false,
    @ColumnInfo val imageUrl: String ?,
    @ColumnInfo val inUse: Boolean = false,
    @ColumnInfo val maidenFlight: String ?,
    @PrimaryKey(autoGenerate = false) val name: String = "",
    @ColumnInfo val countryCode: String = "",
    @ColumnInfo var isInFavorites: Boolean = false,
    @ColumnInfo var wasSeen: Boolean = false
   // @ColumnInfo val nationUrl: String = ""
    ): Parcelable


