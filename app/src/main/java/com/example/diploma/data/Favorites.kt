package com.example.diploma.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "favorites", indices = [Index(value = ["name"], unique = true)])
data class Favorites(
    @ColumnInfo val agency: String = "",
    @ColumnInfo val capability: String = "",
    @ColumnInfo var crewCapacity: Int = 0,
    @ColumnInfo var humanRated: Boolean = false,
    @ColumnInfo var imageUrl: String ?,
    @ColumnInfo val inUse: Boolean = false,
    @ColumnInfo var maidenFlight: String ?,
    @PrimaryKey(autoGenerate = false) var name: String = "",
    @ColumnInfo var countryCode: String = "",
    ): Parcelable
