package com.example.playlistmaker.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(defaultValue = "")
    val title: String?,
    val description: String?,
    val imagePath: String?,
    @ColumnInfo(defaultValue = "0")
    val trackAmount: Int = 0
)
