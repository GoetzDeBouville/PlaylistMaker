package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.example.playlistmaker.track.Track
import com.google.gson.Gson

const val HISTORY_TRACKLIST = "history_tracklist"

class SearchHistory(private val sharedPreferences: SharedPreferences) {
    val historyTrackList = mutableListOf<Track>()
    private val gson = Gson()

    init {
        sharedPreferences.getString(HISTORY_TRACKLIST, "")?.let {
            if (it.isNotEmpty()) historyTrackList.addAll(fromJsonToTracklist(it))
        }
    }

    fun fromJsonToTracklist(json: String): List<Track> =
        Gson().fromJson(json, Array<Track>::class.java).toList()


    private fun Array<Track>.fromTracklistToJson(): String =
        gson.toJson(this)

    @SuppressLint("CommitPrefEdits")
    fun addTrack(track: Track) {
        if(track in historyTrackList) {
            historyTrackList.remove(track)
        } else if (historyTrackList.size == 10) {
            historyTrackList.removeLast()
        }
        historyTrackList.add(0, track)
        sharedPreferences.edit()
            .putString(HISTORY_TRACKLIST, historyTrackList.toTypedArray().fromTracklistToJson())
            .apply()
    }

    fun clearHistory() {
        historyTrackList.clear()
        sharedPreferences.edit()
            .remove(HISTORY_TRACKLIST)
            .apply()
    }
}
