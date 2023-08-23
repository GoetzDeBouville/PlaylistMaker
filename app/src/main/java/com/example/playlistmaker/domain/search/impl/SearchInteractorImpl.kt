package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.api.SearchInteractor
import com.example.playlistmaker.domain.search.api.SearchRepository
import com.example.playlistmaker.domain.Resource
import java.util.concurrent.Executors

class SearchInteractorImpl(
    private val repository: SearchRepository
) : SearchInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(expression: String, consumer: SearchInteractor.TracksConsumer) {
        executor.execute {
            when (val resource = repository.searchTracks(expression)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.errorType)
                }
            }
        }
    }
}
