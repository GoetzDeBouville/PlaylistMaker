package com.example.playlistmaker.di

import android.content.Context
import androidx.room.Room
import com.example.playlistmaker.data.player.PlayerImpl
import com.example.playlistmaker.data.search.network.ItunesAPI
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.storage.History
import com.example.playlistmaker.data.search.storage.shared_preferences.SharedPreferencesHistoryStorage
import com.example.playlistmaker.data.sharing.impl.ContentProviderImpl
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.db.AppDatabase
import com.example.playlistmaker.domain.player.Player
import com.example.playlistmaker.domain.sharing.ContentProvider
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<ItunesAPI> {
        Retrofit.Builder()
            .baseUrl(RetrofitNetworkClient.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesAPI::class.java)
    }
    single {
        androidContext()
            .getSharedPreferences("pm_prefs", Context.MODE_PRIVATE)
    }
    factoryOf(::Gson)
    singleOf(::RetrofitNetworkClient) { bind<NetworkClient>() }
    factoryOf(::SharedPreferencesHistoryStorage) { bind<History>() }
    singleOf(::ContentProviderImpl) { bind<ContentProvider>() }
    singleOf(::ExternalNavigatorImpl) { bind<ExternalNavigator>() }
    factoryOf(::PlayerImpl) { bind<Player>() }
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "favorite_tracks.db")
            .build()
    }
    single { get<AppDatabase>().playlistTracksDao() }
    single { get<AppDatabase>().savedTracksDao() }
}
