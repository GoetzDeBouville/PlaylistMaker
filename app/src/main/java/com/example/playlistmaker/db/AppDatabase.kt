package com.example.playlistmaker.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.db.dao.PlaylistDao
import com.example.playlistmaker.db.dao.TrackDao
import com.example.playlistmaker.db.entity.PlaylistEntity
import com.example.playlistmaker.db.entity.TrackEntity

@Database(
    version = 4,
    entities = [TrackEntity::class, PlaylistEntity::class],
    autoMigrations = [AutoMigration(3, 4)],
    exportSchema = true
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}
