package com.example.playlistmaker.di

import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.db.impl.FavoriteTracksInteractorImpl
import com.example.playlistmaker.domain.db.impl.PlaylistInteractorImpl
import com.example.playlistmaker.domain.search.api.HistoryInteractor
import com.example.playlistmaker.domain.search.api.SearchInteractor
import com.example.playlistmaker.domain.search.impl.HistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val interactorModule = module {
    singleOf(::SearchInteractorImpl) { bind<SearchInteractor>() }
    singleOf(::HistoryInteractorImpl) { bind<HistoryInteractor>() }
    singleOf(::SettingsInteractor)
    singleOf(::SharingInteractorImpl) { bind<SharingInteractor>() }
    singleOf(::FavoriteTracksInteractorImpl) { bind<FavoriteTracksInteractor>() }
    singleOf(::PlaylistInteractorImpl) { bind<PlaylistInteractor>() }
}
