package com.example.playlistmaker.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.db.dao.PlaylistDao
import com.example.playlistmaker.db.dao.PlaylistTracksDao
import com.example.playlistmaker.db.dao.SavedTrackDao
import com.example.playlistmaker.db.dao.TrackDao
import com.example.playlistmaker.db.entity.PlaylistEntity
import com.example.playlistmaker.db.entity.PlaylistTracksEntity
import com.example.playlistmaker.db.entity.SavedTrackEntity
import com.example.playlistmaker.db.entity.TrackEntity

@Database(
    version = 6,
    entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTracksEntity::class, SavedTrackEntity::class],
    autoMigrations = [AutoMigration(1, 6)],
    exportSchema = true
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun playlistTracksDao(): PlaylistTracksDao
    abstract fun savedTracksDao(): SavedTrackDao
}
