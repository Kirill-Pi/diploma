package com.example.diploma.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "recently_seen", indices = [Index(value = ["name"], unique = true)])
data class RecentlySeen(
    @ColumnInfo var imageUrl: String ?,
    @PrimaryKey(autoGenerate = false) var name: String = ""
    ): Parcelable
