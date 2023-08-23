package com.example.playlistmaker.di

import com.example.playlistmaker.domain.search.api.HistoryInteractor
import com.example.playlistmaker.domain.search.api.SearchInteractor
import com.example.playlistmaker.domain.search.impl.HistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<SearchInteractor> { SearchInteractorImpl(get()) }
    single<HistoryInteractor> { HistoryInteractorImpl(get()) }

    single { SettingsInteractor(get()) }
    single<SharingInteractor> { SharingInteractorImpl(get(), get()) }
}
