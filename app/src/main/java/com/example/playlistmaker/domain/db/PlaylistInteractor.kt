package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun addNewPlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun removePlaylist(playlist: Playlist)
}