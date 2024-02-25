package com.example.playlistmaker.di

import com.example.playlistmaker.data.converters.PlaylistDbConverter
import com.example.playlistmaker.data.converters.TrackDbConverter
import com.example.playlistmaker.data.converters.SavedTrackDbConverter
import com.example.playlistmaker.data.db.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.data.db.PlaylistRepositoryImpl
import com.example.playlistmaker.data.search.repository.HistoryRepositoryImpl
import com.example.playlistmaker.data.search.repository.SearchRepositoryImpl
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.domain.db.FavoriteTracksRepository
import com.example.playlistmaker.domain.db.PlaylistRepository
import com.example.playlistmaker.domain.search.api.HistoryRepository
import com.example.playlistmaker.domain.search.api.SearchRepository
import com.example.playlistmaker.domain.settings.SettingsRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::HistoryRepositoryImpl) { bind<HistoryRepository>() }
    singleOf(::SearchRepositoryImpl) { bind<SearchRepository>() }
    singleOf(::SettingsRepositoryImpl) { bind<SettingsRepository>() }
    singleOf(::FavoriteTracksRepositoryImpl) { bind<FavoriteTracksRepository>() }
    singleOf(::PlaylistRepositoryImpl) { bind<PlaylistRepository>() }

    factoryOf(::TrackDbConverter)
    factoryOf(::PlaylistDbConverter)
    factoryOf(::SavedTrackDbConverter)
}
