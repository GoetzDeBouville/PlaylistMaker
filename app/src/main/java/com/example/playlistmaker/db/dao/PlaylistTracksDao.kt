package com.example.playlistmaker.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.db.entity.PlaylistTracksEntity

@Dao
interface PlaylistTracksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistTrack(playlistTrack: PlaylistTracksEntity)

    @Query("DELETE FROM playlist_tracks_table WHERE playlistId = :playlistId")
    suspend fun removePlaylistTrack(playlistId: Int)

    @Query("DELETE FROM playlist_tracks_table WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun removeTrackFromPlaylistTrack(playlistId: Int, trackId: Int)

    @Query("SELECT * FROM playlist_tracks_table WHERE trackId = :trackId")
    suspend fun getTracksById(trackId: Int): List<PlaylistTracksEntity>

    @Query("SELECT * FROM playlist_tracks_table WHERE playlistId = :playlistId")
    suspend fun getTracksByPlaylistId(playlistId: Int): List<PlaylistTracksEntity>
}
