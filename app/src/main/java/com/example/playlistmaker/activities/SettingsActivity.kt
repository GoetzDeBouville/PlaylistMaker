package com.example.playlistmaker.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivitySettingsBinding
    companion object {
        private const val SHARED_PREFERENCES_NAME = "app_preferences"
        private const val THEME_KEY = "theme_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)

        binding.themeSwitcher.setChecked(sharedPreferences.getBoolean(THEME_KEY, false))

        binding.arrowBack.setOnClickListener { finish() }
        binding.shareApp.setOnClickListener { shareApp() }
        binding.textToSupport.setOnClickListener { sendEmail() }
        binding.userAgreement.setOnClickListener { readOffer() }
        binding.themeSwitcher.setOnCheckedChangeListener { isChecked ->
            (applicationContext as App).switchTheme(isChecked)
            saveTheme(isChecked)
        }
    }

    private fun saveTheme(isDarkTheme: Boolean) {
        sharedPreferences.edit().putBoolean(THEME_KEY, isDarkTheme).apply()
    }

    private fun shareApp() {
        Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, getString(R.string.android_practicum_link))
            startActivity(Intent.createChooser(this, getString(R.string.share_app)))
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun sendEmail() {
        val intentSend = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.target_email)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.text_message))
        }
        if (intentSend.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(intentSend, getString(R.string.text_email_app)))
        } else {
            Toast.makeText(this, getString(R.string.text_no_email_app), Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun readOffer() {
        val intentOffer = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(getString(R.string.practicum_offer_link))
        }
        startActivity(intentOffer)
    }
}