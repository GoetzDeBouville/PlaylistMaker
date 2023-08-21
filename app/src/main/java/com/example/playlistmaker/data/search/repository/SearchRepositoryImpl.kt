package com.example.playlistmaker.data.search.repository

import com.example.playlistmaker.data.search.dto.TracksSearchRequest
import com.example.playlistmaker.data.search.dto.TracksSearchResponse
import com.example.playlistmaker.data.search.mappers.TrackMapper
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.domain.search.api.SearchRepository
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.LoadingStatus
import com.example.playlistmaker.domain.Resource


class SearchRepositoryImpl(
    private val networkClient: NetworkClient
) : SearchRepository {
    private val mapper = TrackMapper()
    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return when (response.resultCode) {
            NO_INTERNET_CODE -> Resource.Error(LoadingStatus.NO_INTERNET)
            SUCCESS_CODE -> Resource.Success((response as TracksSearchResponse).tracks.map {
                mapper.mapDtoToEntity(it)
            })
            else -> Resource.Error(LoadingStatus.SERVER_ERROR)
        }
    }

    companion object {
        const val NO_INTERNET_CODE = -1
        const val SUCCESS_CODE = 200
    }
}
