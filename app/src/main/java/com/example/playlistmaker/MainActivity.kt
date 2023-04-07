package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchButton = findViewById<Button>(R.id.search)
        val mediaButton = findViewById<Button>(R.id.media)
        val settingsButton = findViewById<Button>(R.id.settings)

        searchButton.setOnClickListener{
            val searchIntent = Intent(this, Search::class.java)
            startActivity(searchIntent)
        }

        val mediaButtonClickListener : View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val mediaIntent = Intent(this@MainActivity, Media::class.java)
                startActivity(mediaIntent)
            }
        }
        mediaButton.setOnClickListener(mediaButtonClickListener)

        settingsButton.setOnClickListener {
            val settingsIntent = Intent(this, Settings::class.java)
            startActivity(settingsIntent)
        }
    }
}