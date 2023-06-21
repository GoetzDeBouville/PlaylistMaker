package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.example.playlistmaker.track.Track
import com.google.gson.Gson

const val HISTORY_TRACKLIST = "history_tracklist"

class SearchHistory(private val sharedPreferences: SharedPreferences) {
    val savedInHistoryTracks = mutableListOf<Track>()
    private val gson = Gson()

    init {
        sharedPreferences.getString(HISTORY_TRACKLIST, "")?.let {
            if (it.isNotEmpty()) savedInHistoryTracks.addAll(fromJsonToTracklist(it))
        }
    }

    fun fromJsonToTracklist(json: String): List<Track> =
        Gson().fromJson(json, Array<Track>::class.java).toList()


    private fun Array<Track>.fromTracklistToJson(): String =
        gson.toJson(this)

    @SuppressLint("CommitPrefEdits")
    fun addTrack(track: Track) {
        if (track in savedInHistoryTracks) {
            savedInHistoryTracks.remove(track)
        } else if (savedInHistoryTracks.size == 10) {
            savedInHistoryTracks.removeLast()
        }
        savedInHistoryTracks.add(0, track)
        sharedPreferences.edit()
            .putString(HISTORY_TRACKLIST, savedInHistoryTracks.toTypedArray().fromTracklistToJson())
            .apply()
    }

    fun clearHistory() {
        savedInHistoryTracks.clear()
        sharedPreferences.edit()
            .remove(HISTORY_TRACKLIST)
            .apply()
    }
}
