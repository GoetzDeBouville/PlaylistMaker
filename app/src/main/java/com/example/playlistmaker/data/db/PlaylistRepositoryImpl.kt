package com.example.playlistmaker.data.db

import com.example.playlistmaker.data.converters.PlaylistDbConverter
import com.example.playlistmaker.data.converters.SavedTrackDbConverter
import com.example.playlistmaker.db.AppDatabase
import com.example.playlistmaker.db.entity.PlaylistEntity
import com.example.playlistmaker.db.entity.PlaylistTracksEntity
import com.example.playlistmaker.db.entity.SavedTrackEntity
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val savedTrackDbConverter: SavedTrackDbConverter,
) : PlaylistRepository {
    override suspend fun addNewPlaylist(playlist: Playlist) {
        val playlistEntity = playlistDbConverter.map(playlist).copy()
        appDatabase.playlistDao().insertPlaylist(playlistEntity)
    }

    override suspend fun addTrackToPlayList(playlist: Playlist, track: Track): Flow<Boolean> =
        flow {
            if (appDatabase.playlistTracksDao().getPlaylistTracksById(playlist.id, track.trackId)
                    .isNotEmpty()
            ) {
                emit(false)
                return@flow
            }
            val savedTrackEntity =
                savedTrackDbConverter.map(track).copy(timeStamp = System.currentTimeMillis())
            appDatabase.savedTracksDao().insertSavedTrack(savedTrackEntity)

            val playlistTrack = PlaylistTracksEntity(playlist.id, track.trackId)
            appDatabase.playlistTracksDao().insertPlaylistTrack(playlistTrack)
            val trackList = getTracks(playlist.id).first()
            playlist.trackAmount = trackList.size

            updatePlaylist(playlist)
            emit(true)
        }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return flow {
            val playlists = appDatabase.playlistDao().getPlaylists()
            emit(convertFromEntity(playlists))
        }.flowOn(Dispatchers.IO)
    }

    override fun getTracks(playlistId: Int): Flow<List<Track>> {
        return flow {
            val savedTracks = mutableListOf<SavedTrackEntity>()
            val playlistTrackList =
                appDatabase.playlistTracksDao().getTracksByPlaylistId(playlistId)
            playlistTrackList.forEach {
                appDatabase.savedTracksDao().getTrackById(it.trackId)
                    ?.let { it1 -> savedTracks.add(it1) }
            }
            emit(convertTracksFromEntity(savedTracks))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getTrackRelations(trackId: Int): Int {
        val playlistTrackList = appDatabase.playlistTracksDao().getTracksById(trackId)
        return playlistTrackList.size
    }

    override suspend fun removePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().removePlaylist(playlist.id)
    }


    override suspend fun removeSavedTrackFromPlaylist(playlist: Playlist, track: Track) {
        appDatabase.playlistTracksDao().removeTrackFromPlaylistTrack(playlist.id, track.trackId)
        val trackList = getTracks(playlist.id).first()
        playlist.trackAmount = trackList.size
        updatePlaylist(playlist)
        if (appDatabase.playlistTracksDao().getTracksById(track.trackId).isEmpty()) {
            appDatabase.savedTracksDao().removeSavedTrack(track.trackId)
        }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(playlistDbConverter.map(playlist))
    }

    private fun convertFromEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map {
            playlistDbConverter.map(it)
        }
    }

    private fun convertTracksFromEntity(tracks: List<SavedTrackEntity>): List<Track> {
        return tracks.map { track ->
            savedTrackDbConverter.map(track)
        }
    }
}
