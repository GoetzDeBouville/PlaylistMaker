package com.example.playlistmaker.di

import com.example.playlistmaker.data.search.repository.HistoryRepositoryImpl
import com.example.playlistmaker.data.search.repository.SearchRepositoryImpl
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.domain.search.api.HistoryRepository
import com.example.playlistmaker.domain.search.api.SearchRepository
import com.example.playlistmaker.domain.settings.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single<SearchRepository> { SearchRepositoryImpl(get()) }
    single<HistoryRepository> { HistoryRepositoryImpl(get()) }

    single<SettingsRepository> { SettingsRepositoryImpl(androidContext()) }
}
