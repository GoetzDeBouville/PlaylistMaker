package com.example.playlistmaker.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.db.entity.SavedTrackEntity

@Dao
interface SavedTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSavedTrack(savedTrack: SavedTrackEntity)

    @Query("SELECT * FROM saved_tracks_table WHERE trackId = :trackId")
    suspend fun getTrackById(trackId: Int): SavedTrackEntity?

    @Query("DELETE FROM saved_tracks_table WHERE trackId = :trackId")
    suspend fun removeSavedTrack(trackId: Int)
}
