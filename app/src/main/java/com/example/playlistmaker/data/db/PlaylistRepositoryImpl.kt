package com.example.playlistmaker.data.db

import com.example.playlistmaker.data.converters.PlaylistDbConverter
import com.example.playlistmaker.db.AppDatabase
import com.example.playlistmaker.db.entity.PlaylistEntity
import com.example.playlistmaker.db.entity.TrackEntity
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter
) : PlaylistRepository {
    override suspend fun addNewPlaylist(playlist: Playlist) {
        val playlistEntity = playlistDbConverter.map(playlist).copy()
        appDatabase.playlistDao().insertPlaylist(playlistEntity)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return flow {
            val playlists = appDatabase.playlistDao().getPlaylists()
            emit(convertFromEntity(playlists))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun removePlaylist(playlist: Playlist) {
        val playlistEntity = playlistDbConverter.map(playlist)
        appDatabase.playlistDao().removePlaylist(playlistEntity.id)
    }

    private fun convertFromEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { track ->
            playlistDbConverter.map(track)
        }
    }
}
