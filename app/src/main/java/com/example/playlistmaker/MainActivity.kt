package com.example.playlistmaker

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
            Toast.makeText(this, "Кнопка поиска", Toast.LENGTH_SHORT).show()
        }

        val mediaButtonClickListener : View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                Toast.makeText(this@MainActivity, "Кнопка Медиатека", Toast.LENGTH_SHORT).show()
            }
        }
        mediaButton.setOnClickListener(mediaButtonClickListener)

        settingsButton.setOnClickListener {
            Toast.makeText(this, "Кнопка Настройки", Toast.LENGTH_SHORT).show()
        }
    }
}