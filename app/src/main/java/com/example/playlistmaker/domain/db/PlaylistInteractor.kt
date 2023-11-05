package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun addNewPlaylist(playlist: Playlist)
    suspend fun addTrackToPlayList(playlist: Playlist, track: Track): Flow<Boolean>
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun getTracks(playlistId: Int) : Flow<List<Track>>
    suspend fun playlistDuration(tracks: List<Track>) : String
    suspend fun removePlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
}
