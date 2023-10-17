package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class FavoriteTracksInteractorImpl(private val favoriteTracksRepository: FavoriteTracksRepository) :
    FavoriteTracksInteractor {
    override suspend fun addToFavoriteTrackList(track: Track) {
        favoriteTracksRepository.addTrackToFavorites(track)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return favoriteTracksRepository.getFavoriteTracks().flowOn(Dispatchers.IO)
    }

    override suspend fun isFavoriteTrack(track: Track): Boolean {
        return favoriteTracksRepository.isFavoriteTrack(track)
    }

    override suspend fun removeFromFavoriteTrackList(track: Track) {
        favoriteTracksRepository.removeTrackFromFavorites(track)
    }
}
