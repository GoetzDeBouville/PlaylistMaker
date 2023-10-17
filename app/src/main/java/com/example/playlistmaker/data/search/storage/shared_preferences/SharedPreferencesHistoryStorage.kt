package com.example.playlistmaker.data.search.storage.shared_preferences

import android.content.SharedPreferences
import com.example.playlistmaker.data.search.mappers.TrackMapper
import com.example.playlistmaker.data.search.storage.History
import com.example.playlistmaker.domain.search.models.Track

class SharedPreferencesHistoryStorage(private val sharedPrefs: SharedPreferences) : History {
    private var savedTracks = mutableListOf<Track>()
    private val mapper = TrackMapper()

    override fun clearHistory() {
        savedTracks.clear()
        sharedPrefs.edit()
            .remove(SEARCH_KEY)
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

    override fun saveTrack(track: Track) {
        val existingTrack = savedTracks.find { it.trackId == track.trackId }
        if (existingTrack != null) {
            savedTracks.remove(existingTrack)
        }
        savedTracks.add(0, track)
        if (savedTracks.size > TRACKLIST_SIZE) {
            savedTracks.removeLast()
        }
        sharedPrefs.edit()
            .putString(
                SEARCH_KEY,
                mapper.createJsonFromTracksList(savedTracks.toTypedArray())
            )
            .apply()
    }

    companion object {
        const val SEARCH_KEY = "search_key"
        const val TRACKLIST_SIZE = 10
    }
}
