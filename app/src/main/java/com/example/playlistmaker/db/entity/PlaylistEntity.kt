package com.example.playlistmaker.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String?,
    val imagePath: String?,
    var trackIds: String?,
    var trackAmount: Int = 0
)
