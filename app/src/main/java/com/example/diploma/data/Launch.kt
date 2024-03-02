package com.example.diploma.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "cached_launches", indices = [Index(value = ["name"], unique = true)])
data class Launch(
    @PrimaryKey (autoGenerate = false) val name: String = "",
    @ColumnInfo val net: String ="",
    @ColumnInfo val launchServiceProviderName: String ="",
    @ColumnInfo val rocketFullName: String ="",
    @ColumnInfo val missionName: String ="",
    @ColumnInfo val missionDescription: String ="",
    @ColumnInfo val agenciesName: String ="",
    @ColumnInfo val agenciesCountry: String="",
    @ColumnInfo val padName: String="",
    @ColumnInfo val padLocation: String="",
    @ColumnInfo val status: Boolean = true,
    @ColumnInfo val rocket: String ="",
    @ColumnInfo val image: String?
    ): Parcelable






