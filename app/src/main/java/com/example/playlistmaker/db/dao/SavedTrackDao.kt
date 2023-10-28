package com.example.playlistmaker.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.db.entity.SavedTrackEntity
import com.example.playlistmaker.db.entity.TrackEntity

@Dao
interface SavedTrackDao {
    @Delete(entity = SavedTrackEntity::class)
    suspend fun deleteTrack(trackEntity: SavedTrackEntity)

    @Query("SELECT trackId FROM tracks_table")
    fun getTrackById(): SavedTrackEntity

    @Query("SELECT * FROM tracks_table ORDER BY timeStamp DESC")
    fun getTracks(): List<SavedTrackEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(trackEntity: SavedTrackEntity)
}
