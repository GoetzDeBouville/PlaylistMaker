package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteTracksInteractorImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository
) :
    FavoriteTracksInteractor {
    override fun getFavoriteTracks(): Flow<List<Track>> {
        return favoriteTracksRepository.getFavoriteTracks().map { tracks ->
            tracks.reversed()
        }
    }

    override suspend fun addToFavoriteTrackList(track: Track) {
        favoriteTracksRepository.addTrackToFavorites(track)
    }

    override suspend fun removeFromFavoriteTrackList(track: Track) {
        favoriteTracksRepository.removeTrackFromFavorites(track)
    }
}