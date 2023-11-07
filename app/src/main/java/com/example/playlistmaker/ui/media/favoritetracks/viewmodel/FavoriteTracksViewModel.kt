package com.example.playlistmaker.ui.media.favoritetracks.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.media.models.FavoriteTracksState
import com.example.playlistmaker.utils.Tools
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(private val favoriteTracksInteractor: FavoriteTracksInteractor) :
    ViewModel() {

    private val _state = MutableLiveData<FavoriteTracksState>()
    val state: LiveData<FavoriteTracksState>
        get() = _state
    private var isClickAllowed = true

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(Tools.CLICK_DEBOUNCE_DELAY_MS)
                isClickAllowed = true
            }
        }
        return current
    }

    fun getFavoriteTracks() {
        viewModelScope.launch {
            favoriteTracksInteractor.getFavoriteTracks().collect {
                if (it.isEmpty()) _state.postValue(FavoriteTracksState.Empty)
                else _state.postValue(FavoriteTracksState.Content(it))
            }
        }
    }
}
