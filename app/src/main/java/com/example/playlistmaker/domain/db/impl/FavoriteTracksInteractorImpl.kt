package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository
) :
    FavoriteTracksInteractor {
    override fun favoriteTracks(): Flow<List<Track>> {
        return favoriteTracksRepository.favoriteTracks()
    }

    override suspend fun addToFavoriteTrackList(track: Track) {
        favoriteTracksRepository.addTrackToFavorites(track)
    }

    override suspend fun removeFromFavoriteTrackList(track: Track) {
        favoriteTracksRepository.removeTrackFromFavorites(track)
    }
}