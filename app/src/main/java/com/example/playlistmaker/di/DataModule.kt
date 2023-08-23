package com.example.playlistmaker.di

import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.storage.History
import com.example.playlistmaker.data.search.storage.shared_preferences.SharedPreferencesHistoryStorage
import com.example.playlistmaker.data.sharing.impl.ContentProviderImpl
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.domain.sharing.ContentProvider
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<NetworkClient> { RetrofitNetworkClient(androidContext()) }

    factory<History> { SharedPreferencesHistoryStorage(get()) }

    single<ContentProvider> { ContentProviderImpl(androidContext()) }

    single<ExternalNavigator> { ExternalNavigatorImpl(androidContext()) }
}
