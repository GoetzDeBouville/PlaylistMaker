package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val arrowBackButton = findViewById<ImageView>(R.id.arrow_back)

        arrowBackButton.setOnClickListener {
            val intent = Intent(this@Settings, MainActivity::class.java)
            startActivity(intent)
        }
    }
}