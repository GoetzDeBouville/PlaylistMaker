package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import com.example.playlistmaker.track.Track
import com.google.gson.Gson
import java.util.LinkedList

const val HISTORY_TRACKLIST = "history_tracklist"

class SearchHistory(private val sharedPreferences: SharedPreferences) {
    val historyTrackList = LinkedList<Track>()
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
        if (historyTrackList.size == 10) {
            historyTrackList.removeLast()
        }
        historyTrackList.addFirst(track)
        Log.e("SearchHistory.add", historyTrackList.toString())
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
