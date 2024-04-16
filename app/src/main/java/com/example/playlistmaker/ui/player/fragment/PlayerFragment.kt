package com.example.playlistmaker.ui.player.fragment

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.playlistmaker.R
import com.example.playlistmaker.core.ui.BaseFragment
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.media.models.AddToPlaylist
import com.example.playlistmaker.domain.media.models.PlaylistState
import com.example.playlistmaker.domain.player.PlayerControl
import com.example.playlistmaker.domain.player.models.PlayerState
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.player.adapter.PlaylistAdapter
import com.example.playlistmaker.ui.player.service.PlayerService
import com.example.playlistmaker.ui.player.viewmodel.PlayerViewModel
import com.example.playlistmaker.ui.search.fragment.SearchFragment
import com.example.playlistmaker.utils.NetworkStatusReciever
import com.example.playlistmaker.utils.Tools
import com.example.playlistmaker.utils.applyBlurEffect
import com.example.playlistmaker.utils.clearBlurEffect
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment :
    BaseFragment<FragmentPlayerBinding, PlayerViewModel>(FragmentPlayerBinding::inflate) {
    override val viewModel: PlayerViewModel by viewModel { parametersOf(track) }
    private var track: Track? = null
    private val playlistAdapter = PlaylistAdapter { selectedPlaylist ->
        viewModel.addTrackToPlayList(selectedPlaylist, track!!)
    }
    private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    private val networkStatusReciever = NetworkStatusReciever()

    private var playerControl: PlayerControl? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayerService.PlayerServiceBinder
            playerControl = binder.getService()
            viewModel.playerControlManager(binder.getService())
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.removePlayerControl()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            bindMusicService()
        } else {
            Tools.showSnackbar(binding.root, getString(R.string.no_permission), requireContext())
        }
    }

    private fun bindMusicService() {
        val intent = Intent(requireContext(), PlayerService::class.java).putExtra(
            SearchFragment.TRACK_KEY,
            track
        )
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun initViews() {
        getTrack()
        fetchPlayer()
        initBottomSheet()
        viewModel.getPlaylists()
    }

    override fun subscribe() {
        observeViewModel()

        clickListeners()
        addingToPlaylistStateObserver(bottomSheetBehavior!!)
    }

    override fun onStart() {
        super.onStart()
        viewModel.hideNotification()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTrack()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            bindMusicService()
        }
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(networkStatusReciever)
    }

    override fun onResume() {
        super.onResume()
        ContextCompat.registerReceiver(
            requireContext(),
            networkStatusReciever,
            IntentFilter(NetworkStatusReciever.ACTION),
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    override fun onStop() {
        super.onStop()
        viewModel.showNotification()
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomSheetBehavior = null
        viewModel.pausePlayer()
        viewModel.hideNotification()
        requireContext().unbindService(serviceConnection)
    }

    private fun getTrack() {
        val trackJson = arguments?.getString(SearchFragment.TRACK_KEY)
        val gson = Gson()
        track = gson.fromJson(trackJson, Track::class.java)
    }

    private fun initBottomSheet() {
        val bottomSheetContainer = binding.llStandardBottomSheet
        bottomSheetBehavior =
            BottomSheetBehavior.from(bottomSheetContainer).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        bottomSheetObserver(bottomSheetBehavior!!, binding.overlay)
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

    private fun clickListeners() = with(binding) {
        val listener = onClickListener()
        ivArrowBack.setOnClickListener(listener)
        playbackController.setOnClickListener(listener)
        ivLikeButton.setOnClickListener(listener)
        btnBottomSheet.setOnClickListener(listener)
        btnAddToPlaylist.setOnClickListener(listener)
    }

    private fun onClickListener() = View.OnClickListener {
        with(binding) {
            when (it) {
                ivArrowBack -> requireActivity().supportFragmentManager.popBackStack()
                playbackController -> viewModel.playbackControl()
                ivLikeButton -> viewModel.onFavoriteTrackClicked()
                btnBottomSheet -> findNavController().navigate(R.id.action_global_to_newPlaylistFragment)
                btnAddToPlaylist -> bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    private fun fetchPlayer() = with(binding) {
        albumPosterImage.load(track?.getArtwork512()) {
            placeholder(R.drawable.poster_placeholder)
            transformations(
                RoundedCornersTransformation(
                    resources.getDimensionPixelSize(R.dimen.album_cover_corner_radius).toFloat()
                )
            )
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.playerState.collect { state ->
                renderPlayerState(state)
            }
        }
//        viewModel.playerState.observe(viewLifecycleOwner) {
//            renderPlayerState(it)
//        }

        viewModel.timeProgress.observe(viewLifecycleOwner) {
            binding.textTrackTimeValue.text = it
        }

        viewModel.isFavorite.observe(viewLifecycleOwner) {
            manageLikeButtonState(it)
        }

        viewModel.playlistState.observe(viewLifecycleOwner) {
            if (it is PlaylistState.Content) {
                playlistAdapter.playlists.clear()
                playlistAdapter.playlists.addAll(it.playlists)
                playlistAdapter.notifyDataSetChanged()
            }
        }

        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = playlistAdapter
        }
    }

    private fun renderPlayerState(state: PlayerState) = with(binding) {
        when (state) {
            PlayerState.STATE_PLAYING -> {
                playbackController.isClickable = true
                playbackController.clearBlurEffect()
            }

            PlayerState.STATE_PAUSED, PlayerState.STATE_PREPARED -> {
                playbackController.clearBlurEffect()
                playbackController.isClickable = true
                playbackController.setStatusPause()
                viewModel.hideNotification()
            }

            PlayerState.STATE_DEFAULT -> {
                playbackController.applyBlurEffect()
                playbackController.isClickable = false
            }
        }
    }
}
