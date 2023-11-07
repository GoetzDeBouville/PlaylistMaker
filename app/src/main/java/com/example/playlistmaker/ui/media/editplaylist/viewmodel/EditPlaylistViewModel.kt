package com.example.playlistmaker.ui.media.editplaylist.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.ui.media.newplaylist.viewmodel.NewPlaylistViewModel
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : NewPlaylistViewModel(playlistInteractor) {
    fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.updatePlaylist(playlist)
        }
    }
}