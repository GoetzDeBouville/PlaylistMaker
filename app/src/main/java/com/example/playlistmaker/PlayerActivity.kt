package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.track.Track

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = intent.extras?.get(ADDITIONAL_KEY_TRACK) as Track
        setTrackInfoToViews()

        binding.arrowBack.setOnClickListener { finish() }
    }

    private fun setTrackInfoToViews() {
        with(binding) {
            Glide.with(this@PlayerActivity)
                .load(track.getArtwork512())
                .placeholder(R.drawable.poster_placeholder)
                .transform(RoundedCorners(10))
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

    companion object {
        private const val ADDITIONAL_KEY_TRACK = "add_key_track"

        fun newIntent(context: Context, track: Track): Intent {
            return Intent(context, PlayerActivity::class.java).apply {
                putExtra(ADDITIONAL_KEY_TRACK, track)
            }
        }
    }
}
