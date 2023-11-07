package com.example.playlistmaker.di

import com.example.playlistmaker.domain.player.Player
import com.example.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.media.editplaylist.viewmodel.EditPlaylistViewModel
import com.example.playlistmaker.ui.media.favoritetracks.viewmodel.FavoriteTracksViewModel
import com.example.playlistmaker.ui.media.newplaylist.viewmodel.NewPlaylistViewModel
import com.example.playlistmaker.ui.media.playlists.viewmodel.PlaylistsViewModel
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.example.playlistmaker.ui.singleplaylist.viewmodel.SinglePlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::FavoriteTracksViewModel)
    viewModelOf(::PlaylistsViewModel)
    viewModelOf(::NewPlaylistViewModel)
    viewModelOf(::SinglePlaylistViewModel)
    viewModelOf(::EditPlaylistViewModel)
    viewModel { (track: Track) ->
        val player = get<Player> { parametersOf(track) }
        PlayerViewModel(track, PlayerInteractorImpl(player), get(), get())
    }
}
