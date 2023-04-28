package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchButton = findViewById<Button>(R.id.search)
        val mediaButton = findViewById<Button>(R.id.media)
        val settingsButton = findViewById<Button>(R.id.settings)

        searchButton.setOnClickListener {
            val searchIntent = Intent(this, Search::class.java)
            startActivity(searchIntent)
        }

        mediaButton.setOnClickListener {
            val mediaIntent = Intent(this, Media::class.java)
            startActivity(mediaIntent)
        }

        settingsButton.setOnClickListener {
            val settingsIntent = Intent(this, Settings::class.java)
            startActivity(settingsIntent)
        }
    }
}