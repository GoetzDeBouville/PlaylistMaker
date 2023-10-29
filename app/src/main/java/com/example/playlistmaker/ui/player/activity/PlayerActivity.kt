package com.example.playlistmaker.ui.player.activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.media.models.AddToPlaylist
import com.example.playlistmaker.domain.media.models.PlaylistState
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.player.models.PlayerState
import com.example.playlistmaker.ui.player.adapter.PlaylistAdapter
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import org.koin.core.parameter.parametersOf
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding

    private val viewModel: PlayerViewModel by viewModel { parametersOf(track) }
    private var vectorDrawable: VectorDrawable? = null
    private var track: Track? = null
    private val playlistAdapter = PlaylistAdapter{ selectedPlaylist ->
        Log.i("PlayerActivity", "null check track = $track")
        viewModel.addTrackToPlayList(selectedPlaylist, track!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomSheetContainer = binding.standardBottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetObserver(bottomSheetBehavior, binding.overlay)

        track = intent.extras?.get(ADDITIONAL_KEY_TRACK) as Track
        viewModel.getPlaylists()
        vectorDrawable = ContextCompat.getDrawable(this, R.drawable.play_button) as VectorDrawable
        fetchPlayer()
        observeViewModel()
        binding.arrowBack.setOnClickListener { finish() }
        binding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }
        binding.likeButton.setOnClickListener {
            viewModel.onFavoriteTrackClicked()
        }
        binding.addToPlaylistButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    private fun bottomSheetObserver(
        bottomSheetBehavior: BottomSheetBehavior<LinearLayout>,
        overlay: View
    ) {
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) overlay.visibility = View.GONE
                else overlay.visibility = View.VISIBLE
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                overlay.alpha = slideOffset
            }
        })
    }

    private fun fetchPlayer() {
        with(binding) {
            Glide.with(this@PlayerActivity)
                .load(track?.getArtwork512())
                .placeholder(R.drawable.poster_placeholder)
                .transform(
                    RoundedCorners(
                        resources.getDimensionPixelSize(R.dimen.album_cover_corner_radius)
                    )
                )
                .into(albumPosterImage)
            trackName.text = track?.trackName
            trackArtist.text = track?.artistName
            textDurationValue.text = track?.timeFormater()
            if (track?.collectionName != null) {
                textAlbumValue.text = track?.collectionName
            } else {
                textAlbumValue.visibility = View.INVISIBLE
                textAlbum.visibility = View.INVISIBLE
            }
            textGenreValue.text = track?.primaryGenreName
            textCountryValue.text = track?.country
            textYearValue.text = track?.releaseDate
            textYearValue.text = track?.yearFormater()
        }
    }

    private fun manageLikeButtonState(isFavorite: Boolean) {
        if (isFavorite) {
            binding.likeButtonState.setImageDrawable(
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.ic_infavorite
                )
            )
        } else {
            binding.likeButtonState.setImageDrawable(
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.ic_like
                )
            )
        }
    }

    private fun observeViewModel() {
        viewModel.playerState.observe(this) {
            renderState(it)
        }

        viewModel.timeProgress.observe(this) {
            binding.textTrackTimeValue.text = it
        }

        viewModel.isFavorite.observe(this) {
            manageLikeButtonState(it)
        }

        viewModel.playlistState.observe(this) {
            if (it is PlaylistState.Content) {
                playlistAdapter.playlists.clear()
                playlistAdapter.playlists.addAll(it.playlists)
                playlistAdapter.notifyDataSetChanged()
            }
        }

        viewModel.addingState.observe(this) { state ->
            viewModel.selectedPlaylistName.value?.let { playlistName ->
                val message = when (state) {
                    AddToPlaylist.ADDED -> "ADDED to $playlistName"
                    else -> "ALREADY EXIST IN $playlistName"
                }
                showSnackbar(message)
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = playlistAdapter
    }

    private fun renderState(state: PlayerState) {
        when (state) {
            PlayerState.STATE_PLAYING -> showPauseBtn()
            PlayerState.STATE_PAUSED, PlayerState.STATE_PREPARED -> {
                vectorDrawable?.setTint(ContextCompat.getColor(this, R.color.elements_color))
                showPlayBtn()
            }

            PlayerState.STATE_DEFAULT -> {
                vectorDrawable?.setTint(ContextCompat.getColor(this, R.color.prepaing_play_button))
                showOnPrepareMessage()
            }
        }
    }

    private fun showOnPrepareMessage() {
        binding.playButton.setOnClickListener {
            showToast(getString(R.string.player_in_progress))
        }
        binding.playButton.setImageResource(R.drawable.play_button)
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

    private fun showToast(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        val snackTextColor = ContextCompat.getColor(this, R.color.snack_text)
        val backgroundColor = ContextCompat.getColor(this, R.color.text_color)

        val textView =
            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        textView.textSize = 16f
        textView.setTextColor(snackTextColor)
        snackbar.view.setBackgroundColor(backgroundColor)
        snackbar.show()
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
