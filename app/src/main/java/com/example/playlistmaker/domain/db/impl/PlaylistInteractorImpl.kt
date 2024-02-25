package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.utils.Tools
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.TimeUnit

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {
    override suspend fun addNewPlaylist(playlist: Playlist) {
        playlistRepository.addNewPlaylist(playlist)
    }

    override suspend fun addTrackToPlayList(playlist: Playlist, track: Track): Flow<Boolean> {
        return playlistRepository.addTrackToPlayList(playlist, track)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists().flowOn(Dispatchers.IO)
    }

    override suspend fun getTracks(playlistId: Int): Flow<List<Track>> {
        return playlistRepository.getTracks(playlistId)
    }

    override suspend fun playlistDuration(tracks: List<Track>): String {
        val durationMillis = tracks.sumOf { it.trackTimeMillis ?: 0 }
        val durationMinutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis)
        return Tools.durationTextFormater(durationMinutes.toInt())
    }

    override suspend fun removePlaylist(playlist: Playlist) {
        playlistRepository.removePlaylist(playlist)
    }

    override suspend fun removeSavedTrackFromPlaylist(playlist: Playlist, track: Track) {
        playlistRepository.removeSavedTrackFromPlaylist(playlist, track)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }
}