package com.example.playlistmaker.itunesApi

import com.example.playlistmaker.Track
import com.google.gson.annotations.SerializedName

data class ItunesResponce(@SerializedName("results") val tracks: List<Track>)
