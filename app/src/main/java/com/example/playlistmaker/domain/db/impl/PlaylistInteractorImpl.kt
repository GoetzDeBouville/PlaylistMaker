package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.media.models.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) : PlaylistInteractor {
    override suspend fun addNewPlaylist(playlist: Playlist) {
        playlistRepository.addNewPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists().flowOn(Dispatchers.IO)
    }

    override suspend fun removePlaylist(playlist: Playlist) {
        playlistRepository.removePlaylist(playlist)
    }
}