package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.switchmaterial.SwitchMaterial

private val SHARED_PREFERENCES_NAME = "app_preferences"
private val THEME_KEY = "theme_key"
private lateinit var sharedPreferences: SharedPreferences
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val arrowBackButton = findViewById<ImageView>(R.id.arrow_back)
        val shareAppButton = findViewById<LinearLayout>(R.id.share_app)
        val sendEmailButton = findViewById<LinearLayout>(R.id.text_to_support)
        val readOfferLink = findViewById<LinearLayout>(R.id.user_agreement)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        themeSwitcher.isChecked = sharedPreferences.getBoolean(THEME_KEY, false)

        arrowBackButton.setOnClickListener { finish() }
        shareAppButton.setOnClickListener { shareApp() }
        sendEmailButton.setOnClickListener { sendEmail() }
        readOfferLink.setOnClickListener { readOffer() }
        themeSwitcher.setOnCheckedChangeListener { switcher, isChecked ->
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