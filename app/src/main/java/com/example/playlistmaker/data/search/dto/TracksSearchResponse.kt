package com.example.playlistmaker.data.search.dto

import com.google.gson.annotations.SerializedName

data class TracksSearchResponse(@SerializedName("results") val tracks: List<TrackDto>): Response()
