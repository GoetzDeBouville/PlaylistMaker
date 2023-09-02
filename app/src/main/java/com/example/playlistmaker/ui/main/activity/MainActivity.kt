package com.example.playlistmaker.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.ui.media.MediaActivity
import com.example.playlistmaker.ui.search.activity.SearchActivity
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.ui.settings.activity.SettingsActivity
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isClickedAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            handler.postDelayed({ isClickedAllowed = true }, CLICK_DEBOUNCE_DELAY_500MS)
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_500MS = 500L
    }
}
