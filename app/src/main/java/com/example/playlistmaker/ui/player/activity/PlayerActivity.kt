package com.example.playlistmaker.ui.player.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.player.models.PlayerState
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var track: Track
    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = intent.extras?.get(ADDITIONAL_KEY_TRACK) as Track

        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory(track)
        )[PlayerViewModel::class.java]

        fetchPlayer()
        observeViewModel()
        binding.arrowBack.setOnClickListener { finish() }
        binding.playButton.setOnClickListener { viewModel.playbackControl() }
    }

    private fun observeViewModel() {
        viewModel.playerState.observe(this) {
            renderState(it)
        }
        viewModel.timeProgress.observe(this) {
            binding.textTrackTimeValue.text = it
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    private fun renderState(state: PlayerState) {
        when (state) {
            PlayerState.STATE_PLAYING -> showPauseBtn()
            PlayerState.STATE_PAUSED, PlayerState.STATE_PREPARED -> showPlayBtn()
            PlayerState.STATE_DEFAULT -> shoOnPrepareMessage()
        }
    }

    private fun showPauseBtn() {
        binding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }
        binding.playButton.setImageResource(R.drawable.pause_button)
    }

    private fun showPlayBtn() {
        binding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }
        binding.playButton.setImageResource(R.drawable.play_button)
    }

    private fun shoOnPrepareMessage() {
        binding.playButton.setOnClickListener {
            showToast(getString(R.string.player_in_progress))
        }
        binding.playButton.setImageResource(R.drawable.play_button)
    }

    private fun fetchPlayer() {
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

    private fun showToast(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val ADDITIONAL_KEY_TRACK = "add_key_track"
        fun newIntent(context: Context, track: Track): Intent {
            return Intent(context, PlayerActivity::class.java).apply {
                putExtra(ADDITIONAL_KEY_TRACK, track)
            }
        }
    }
}