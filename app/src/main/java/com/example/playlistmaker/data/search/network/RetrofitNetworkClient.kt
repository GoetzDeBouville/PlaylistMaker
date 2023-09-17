package com.example.playlistmaker.data.search.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.data.search.dto.TracksSearchRequest

class RetrofitNetworkClient(private val itunesService: ItunesAPI, private val context: Context) :
    NetworkClient {

    override fun doRequest(dto: Any): Response {
        return try {
            if (!isConnected()) {
                return Response().apply { resultCode = -1 }
            }
            if (dto !is TracksSearchRequest) {
                return Response().apply { resultCode = 400 }
            }

            val response = itunesService.search(dto.expression).execute()
            val body = response.body()
            body?.apply { resultCode = response.code() } ?: Response().apply {
                resultCode = response.code()
            }
        } catch (e: Exception) {
            return Response().apply { resultCode = -2 }
        }
    }

    @SuppressLint("NewApi")
    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }

    companion object {
        const val BASE_URL = "https://itunes.apple.com/"
    }
}
