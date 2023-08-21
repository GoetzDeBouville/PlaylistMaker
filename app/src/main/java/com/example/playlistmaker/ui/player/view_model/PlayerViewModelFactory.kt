package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.domain.search.models.Track

class PlayerViewModelFactory(private val track: Track) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)){
            return PlayerViewModel(track) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class!!")
    }
}
