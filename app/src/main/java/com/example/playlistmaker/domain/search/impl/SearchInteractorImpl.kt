package com.example.playlistmaker.domain.search.impl

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.api.SearchInteractor
import com.example.playlistmaker.domain.search.api.SearchRepository
import com.example.playlistmaker.util.Resource
import java.util.concurrent.Executors


class SearchInteractorImpl(
    private val repository: SearchRepository,
    private val context: Context
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

    override fun getConnectionErrorMessage() : String {
        return context.getString(R.string.check_connection)
    }

    override fun getEmptyListMessage() : String {
        return context.getString(R.string.nothing_found)
    }
}