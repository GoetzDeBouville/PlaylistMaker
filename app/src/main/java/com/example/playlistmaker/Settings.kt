package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val arrowBackButton = findViewById<ImageView>(R.id.arrow_back)
        val shareAppButton = findViewById<ImageView>(R.id.share_app)
        val sendEmailButton = findViewById<ImageView>(R.id.text_to_support)
        val readOfferLink = findViewById<ImageView>(R.id.user_agreement)

        arrowBackButton.setOnClickListener { finish() }
        shareAppButton.setOnClickListener { shareApp() }
        sendEmailButton.setOnClickListener { sendEmail() }
        readOfferLink.setOnClickListener { readOffer() }
    }

    private fun shareApp() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/android-developer/")
        }
        val chooserIntent = Intent.createChooser(shareIntent, getString(R.string.share_app))
        startActivity(chooserIntent)
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun sendEmail() {
        val email = "zinchencko.alexei2018@yandex.ru"
        val subject = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
        val textMessage = "Спасибо разработчикам и разработчицам за крутое приложение!"

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, textMessage)
        }

        if (intent.resolveActivity(packageManager) == null) {
            Toast.makeText(
                this,
                "На устройстве нет приложения электронной почты",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        startActivity(intent)
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun readOffer() {
        val link = "https://yandex.ru/legal/practicum_offer/"

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(link)
        }

        if (intent.resolveActivity(packageManager) == null) { // Проверка наличия браузера
            Toast.makeText(this, "На устройстве нет установленного браузера", Toast.LENGTH_SHORT)
                .show()
            return
        }
        startActivity(intent)
    }
}