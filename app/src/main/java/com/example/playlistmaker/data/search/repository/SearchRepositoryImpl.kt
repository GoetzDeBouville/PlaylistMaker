package com.example.playlistmaker.data.search.repository

import com.example.playlistmaker.data.search.dto.TracksSearchRequest
import com.example.playlistmaker.data.search.dto.TracksSearchResponse
import com.example.playlistmaker.data.search.mappers.TrackMapper
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.db.AppDatabase
import com.example.playlistmaker.domain.search.api.SearchRepository
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.LoadingStatus
import com.example.playlistmaker.domain.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase
) : SearchRepository {
    private val mapper = TrackMapper()
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val favoriteTracksListID = withContext(Dispatchers.IO) {
            appDatabase.trackDao().getFavoriteIdList()
        }
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            NO_INTERNET_CODE -> emit(Resource.Error(LoadingStatus.NO_INTERNET))

            SUCCESS_CODE -> emit(Resource.Success((response as TracksSearchResponse).tracks.map {
                mapper.mapDtoToEntity(it, favoriteTracksListID.contains(it.trackId))
            }))

            else -> emit(Resource.Error(LoadingStatus.SERVER_ERROR))
        }
    }

    companion object {
        const val NO_INTERNET_CODE = -1
        const val SUCCESS_CODE = 200
    }
}
