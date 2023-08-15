package com.example.playlistmaker.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.ui.media.MediaActivity
import com.example.playlistmaker.ui.search.activity.SearchActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.ui.settings.activity.SettingsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityMainBinding

    private var isClickedAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        val isDarkTheme = sharedPreferences.getBoolean(THEME_KEY, false)
        (applicationContext as App).switchTheme(isDarkTheme)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.search.setOnClickListener {
            if (clickDebounce()) startActivity(Intent(this, SearchActivity::class.java))
        }

        binding.media.setOnClickListener {
            if (clickDebounce()) startActivity(Intent(this, MediaActivity::class.java))
        }

        binding.settings.setOnClickListener {
            if (clickDebounce()) startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickedAllowed
        if (isClickedAllowed) {
            isClickedAllowed = false
            handler.postDelayed({ isClickedAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "app_preferences"
        private const val THEME_KEY = "theme_key"
        private const val CLICK_DEBOUNCE_DELAY = 500L
    }
}
