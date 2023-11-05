package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun addNewPlaylist(playlist: Playlist)
    suspend fun addTrackToPlayList(playlist: Playlist, track: Track): Flow<Boolean>
    fun getPlaylists(): Flow<List<Playlist>>
    fun getTracks(playlistId: Int): Flow<List<Track>>
    suspend fun removePlaylist(playlist: Playlist)
    suspend fun removeSavedTrackFromPlaylist(playlist: Playlist, track: Track)
    suspend fun updatePlaylist(playlist: Playlist)
}
