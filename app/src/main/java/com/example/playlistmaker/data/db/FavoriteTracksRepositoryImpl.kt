package com.example.playlistmaker.data.db

import com.example.playlistmaker.data.converters.TrackDbConverter
import com.example.playlistmaker.db.AppDatabase
import com.example.playlistmaker.db.entity.TrackEntity
import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
) : FavoriteTracksRepository {
    override fun getFavoriteTracks(): Flow<List<Track>> {
        return flow {
            val tracks = appDatabase.trackDao().getFavoriteTracks()
            emit(convertFromEntity(tracks))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun addTrackToFavorites(track: Track) {
        val trackEntity = trackDbConverter.map(track).copy(timeStamp = System.currentTimeMillis())
        appDatabase.trackDao().insertTrack(trackEntity)
    }

    override suspend fun removeTrackFromFavorites(track: Track) {
        val trackEntity = trackDbConverter.map(track)
        appDatabase.trackDao().deleteTrack(trackEntity)
    }

    override suspend fun isFavoriteTrack(track: Track): Boolean = withContext(Dispatchers.IO) {
        val tracks = appDatabase.trackDao().getFavoriteIdList()
        tracks.contains(track.trackId)
    }

    private fun convertFromEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track ->
            trackDbConverter.map(track)
        }
    }
}
