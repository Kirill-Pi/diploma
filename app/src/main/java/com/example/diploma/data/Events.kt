package com.example.diploma.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "cached_events", indices = [Index(value = ["name"], unique = true)])
data class Events(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val name: String = "",
    @ColumnInfo val date: String = "",
    @ColumnInfo val description: String = "",
    @ColumnInfo val image: String = "",
    @ColumnInfo val location: String = "",
    @ColumnInfo val agencyName: String = ""
    ): Parcelable










