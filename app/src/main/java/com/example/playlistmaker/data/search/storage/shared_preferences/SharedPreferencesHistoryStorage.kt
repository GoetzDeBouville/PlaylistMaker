package com.example.playlistmaker.data.search.storage.shared_preferences

import android.content.Context
import com.example.playlistmaker.data.search.mappers.TrackMapper
import com.example.playlistmaker.data.search.storage.History
import com.example.playlistmaker.domain.search.models.Track

class SharedPreferencesHistoryStorage(context: Context) : History {
    private val savedTracks = mutableListOf<Track>()
    private val mapper = TrackMapper()
    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFERERNCES,
        Context.MODE_PRIVATE
    )

    override fun saveTrack(track: Track) {
        if (savedTracks.contains(track)) {
            savedTracks.remove(track)
        }
        if (savedTracks.size == TRACKLIST_SIZE) {
            savedTracks.removeLast()
        }
        savedTracks.add(0, track)

        sharedPrefs.edit()
            .putString(
                SEARCH_KEY,
                mapper.createJsonFromTracksList(savedTracks.toTypedArray())
            )
            .apply()
    }

    override fun getAllTracks(): List<Track> {
        val tracksString = sharedPrefs.getString(SEARCH_KEY, "")
        if (tracksString?.isNotEmpty() == true) {
            savedTracks.clear()
            savedTracks.addAll(mapper.createTracksListFromJson(tracksString))
        }
        return savedTracks.toList()
    }

    override fun clearHistory() {
        savedTracks.clear()
        sharedPrefs.edit()
            .remove(SEARCH_KEY)
            .apply()
    }

    companion object {
        const val SHARED_PREFERERNCES = "playlist_maker_preferences"
        const val SEARCH_KEY = "search_key"
        const val TRACKLIST_SIZE = 10
    }
}
