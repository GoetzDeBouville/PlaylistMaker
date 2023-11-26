package com.example.playlistmaker.ui.player.fragment

import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.media.models.AddToPlaylist
import com.example.playlistmaker.domain.media.models.PlaylistState
import com.example.playlistmaker.domain.player.models.PlayerState
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.player.adapter.PlaylistAdapter
import com.example.playlistmaker.ui.player.viewmodel.PlayerViewModel
import com.example.playlistmaker.ui.search.fragment.SearchFragment
import com.example.playlistmaker.utils.Tools
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlayerViewModel by viewModel { parametersOf(track) }
    private var vectorDrawable: VectorDrawable? = null
    private var track: Track? = null
    private val playlistAdapter = PlaylistAdapter { selectedPlaylist ->
        viewModel.addTrackToPlayList(selectedPlaylist, track!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetContainer = binding.llStandardBottomSheet
        val bottomSheetBehavior: BottomSheetBehavior<LinearLayout> =
            BottomSheetBehavior.from(bottomSheetContainer).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        bottomSheetObserver(bottomSheetBehavior, binding.overlay)

        track = arguments?.getParcelable(SearchFragment.TRACK_KEY)
        viewModel.getPlaylists()
        vectorDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.play_button) as VectorDrawable
        fetchPlayer()
        observeViewModel()

        binding.btnAddToPlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        clickListeners()
        addingToPlaylistStateObserver(bottomSheetBehavior)
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onResume() {
        super.onResume()
        viewModel.pausePlayer()
    }

    private fun addingToPlaylistStateObserver(bottomSheetBehavior: BottomSheetBehavior<LinearLayout>) {
        viewModel.addingState.observe(requireActivity()) { state ->
            viewModel.selectedPlaylistName.value?.let { playlistName ->
                val message = when (state) {
                    AddToPlaylist.ADDED -> {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        getString(R.string.added_to_playlist, playlistName)
                    }

                    else -> getString(R.string.track_exist_in_playlist, playlistName)
                }
                Tools.showSnackbar(binding.root, message, requireContext())
            }
        }
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

    private fun clickListeners() {
        binding.ivArrowBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.ivPlayButton.setOnClickListener {
            viewModel.playbackControl()
        }
        binding.ivLikeButton.setOnClickListener {
            viewModel.onFavoriteTrackClicked()
        }
        binding.btnBottomSheet.setOnClickListener {
            findNavController().navigate(R.id.action_global_to_newPlaylistFragment)
        }
    }

    private fun fetchPlayer() = with(binding) {
        albumPosterImage.load(track?.getArtwork512()) {
            placeholder(R.drawable.poster_placeholder)
            transformations(RoundedCornersTransformation(resources.getDimensionPixelSize(R.dimen.album_cover_corner_radius).toFloat()))
        }
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

    private fun manageLikeButtonState(isFavorite: Boolean) {
        if (isFavorite) {
            binding.ivLikeButtonState.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_infavorite
                )
            )
        } else {
            binding.ivLikeButtonState.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_like
                )
            )
        }
    }

    private fun observeViewModel() {
        viewModel.playerState.observe(requireActivity()) {
            renderState(it)
        }

        viewModel.timeProgress.observe(requireActivity()) {
            binding.textTrackTimeValue.text = it
        }

        viewModel.isFavorite.observe(requireActivity()) {
            manageLikeButtonState(it)
        }

        viewModel.playlistState.observe(requireActivity()) {
            if (it is PlaylistState.Content) {
                playlistAdapter.playlists.clear()
                playlistAdapter.playlists.addAll(it.playlists)
                playlistAdapter.notifyDataSetChanged()
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = playlistAdapter
    }

    private fun renderState(state: PlayerState) {
        when (state) {
            PlayerState.STATE_PLAYING -> showPauseBtn()
            PlayerState.STATE_PAUSED, PlayerState.STATE_PREPARED -> {
                vectorDrawable?.setTint(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.elements_color
                    )
                )
                showPlayBtn()
            }

            PlayerState.STATE_DEFAULT -> {
                vectorDrawable?.setTint(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.prepaing_play_button
                    )
                )
                showOnPrepareMessage()
            }
        }
    }

    private fun showOnPrepareMessage() {
        binding.ivPlayButton.setOnClickListener {
            Tools.showSnackbar(
                binding.root,
                getString(R.string.player_in_progress),
                requireActivity()
            )
        }
        binding.ivPlayButton.setImageResource(R.drawable.play_button)
    }

    private fun showPauseBtn() {
        binding.ivPlayButton.setOnClickListener {
            viewModel.playbackControl()
        }
        binding.ivPlayButton.setImageResource(R.drawable.pause_button)
    }

    private fun showPlayBtn() {
        binding.ivPlayButton.setOnClickListener {
            viewModel.playbackControl()
        }
        binding.ivPlayButton.setImageResource(R.drawable.play_button)
    }
}
