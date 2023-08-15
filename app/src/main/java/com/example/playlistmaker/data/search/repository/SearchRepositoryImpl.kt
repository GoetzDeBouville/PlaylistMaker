package com.example.playlistmaker.data.search.repository

import com.example.playlistmaker.data.search.dto.TracksSearchRequest
import com.example.playlistmaker.data.search.dto.TracksSearchResponse
import com.example.playlistmaker.data.search.mappers.TrackMapper
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.domain.search.api.SearchRepository
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.util.LoadingStatus
import com.example.playlistmaker.util.Resource


class SearchRepositoryImpl(
    private val networkClient: NetworkClient
) : SearchRepository {
    private val mapper = TrackMapper()
    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> Resource.Error(LoadingStatus.NO_INTERNET)
            200 -> Resource.Success((response as TracksSearchResponse).tracks.map {
                mapper.mapDtoToEntity(it)
            })
            else -> Resource.Error(LoadingStatus.SERVER_ERROR)
        }
    }
}
