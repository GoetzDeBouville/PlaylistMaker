package com.example.playlistmaker.itunesApi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ItunesService {
    private const val BASE_URL = "https://itunes.apple.com/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val itunesService: ItunesAPI = retrofit.create(ItunesAPI::class.java)
}
