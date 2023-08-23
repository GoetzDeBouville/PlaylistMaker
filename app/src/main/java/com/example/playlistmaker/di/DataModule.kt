package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.data.search.mappers.TrackMapper
import com.example.playlistmaker.data.search.network.ItunesAPI
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.storage.History
import com.example.playlistmaker.data.search.storage.shared_preferences.SharedPreferencesHistoryStorage
import com.example.playlistmaker.data.search.storage.shared_preferences.SharedPreferencesHistoryStorage.Companion.SHARED_PREFERERNCES
import com.example.playlistmaker.data.sharing.impl.ContentProviderImpl
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.domain.sharing.ContentProvider
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<NetworkClient> { RetrofitNetworkClient(androidContext()) }

    factory<History> { SharedPreferencesHistoryStorage(get()) }

    single<ContentProvider> { ContentProviderImpl(androidContext()) }

    single<ExternalNavigator> { ExternalNavigatorImpl(androidContext()) }
}