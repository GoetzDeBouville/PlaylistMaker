package com.example.playlistmaker.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    fun getPlaylists(): List<PlaylistEntity>

    @Query("DELETE FROM playlist_table WHERE id = :id")
    suspend fun removePlaylist(id: Int)

    @Update
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)
}
