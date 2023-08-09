package com.example.playlistmaker.player.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.PlayerInteractor
import java.util.Locale
import java.util.concurrent.TimeUnit

class PlayerActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private lateinit var playerInteractor: PlayerInteractor

    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var track: Track

    private var currentTrackTime: Long = 0L
    private var startTime: Long = 0L
    private var timerIsRunning = false


    private var playerState = STATE_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        handler = Handler(Looper.getMainLooper())

        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerInteractor = Creator.providePlayerInteractor()

        track = intent.extras?.get(ADDITIONAL_KEY_TRACK) as Track
        setTrackInfoToViews()
        preparePlayer()

        binding.arrowBack.setOnClickListener { finish() }
        binding.playButton.setOnClickListener { playbackControl() }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerInteractor.releasePlayer()
    }

    private fun setTrackInfoToViews() {
        with(binding) {
            Glide.with(this@PlayerActivity)
                .load(track.getArtwork512())
                .placeholder(R.drawable.poster_placeholder)
                .transform(
                    RoundedCorners(
                        resources.getDimensionPixelSize(R.dimen.album_cover_corner_radius)
                    )
                )
                .into(albumPosterImage)
            trackName.text = track.trackName
            trackArtist.text = track.artistName
            textDurationValue.text = track.timeFormater()
            if (track.collectionName != null) {
                textAlbumValue.text = track.collectionName
            } else {
                textAlbumValue.visibility = View.INVISIBLE
                textAlbum.visibility = View.INVISIBLE
            }
            textGenreValue.text = track.primaryGenreName
            textCountryValue.text = track.country
            textYearValue.text = track.releaseDate
            textYearValue.text = track.yearFormater()
        }
    }

    private fun preparePlayer() {
        playerInteractor.preparePlayer(track) { prepared ->
            if (prepared) {
                playerState = STATE_PREPARED
            } else {
                Toast.makeText(this, "Cannot load media", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startPlayer() {
        playerInteractor.startPlayer {
            binding.playButton.setImageResource(R.drawable.pause_button)
            if (!timerIsRunning) {
                startTime = System.currentTimeMillis() - playerInteractor.getCurrentTrackTime()
                startTrackTimer()
            }
            playerState = playerInteractor.getPlayerState()
        }
    }

    private fun pausePlayer() {
        if (playerState == STATE_PLAYING) {
            playerInteractor.pausePlayer()
            binding.playButton.setImageResource(R.drawable.play_button)
            stopTrackTimer()
            playerState = playerInteractor.getPlayerState()
        }
    }

    private fun playbackControl() {
        when (playerInteractor.getPlayerState()) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private val updateTrackTimeRunnable = object : Runnable {
        override fun run() {
            currentTrackTime = System.currentTimeMillis() - startTime
            binding.textTrackTimeValue.text = formatTrackTime(currentTrackTime)
            handler.postDelayed(this, DELAY)
        }
    }

    private fun formatTrackTime(trackTime: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(trackTime)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(trackTime) % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    private fun startTrackTimer() {
        handler.removeCallbacks(updateTrackTimeRunnable)
        handler.postDelayed(updateTrackTimeRunnable, DELAY)
        timerIsRunning = true
    }

    private fun stopTrackTimer() {
        handler.removeCallbacks(updateTrackTimeRunnable)
        timerIsRunning = false
    }

    companion object {
        private const val ADDITIONAL_KEY_TRACK = "add_key_track"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 10L
        fun newIntent(context: Context, track: Track): Intent {
            return Intent(context, PlayerActivity::class.java).apply {
                putExtra(ADDITIONAL_KEY_TRACK, track)
            }
        }
    }
}
