package com.example.playlistmaker.ui.singleplaylist

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSinglePlaylistBinding
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.domain.media.models.PlaylistTracksState
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.main.BottomNavigationController
import com.example.playlistmaker.ui.media.fragment.PlaylistsFragment
import com.example.playlistmaker.ui.search.fragment.SearchFragment
import com.example.playlistmaker.utils.Tools
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class SinglePlaylist : Fragment() {
    private var _binding: FragmentSinglePlaylistBinding? = null
    private val binding get() = _binding!!
    private var playlist: Playlist? = null
    private val viewModel: SinglePlaylistViewModel by viewModel()
    private var trackCount: Int = 0
    private lateinit var tracks : List<Track>
    private lateinit var adapter: TrackAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomNavigationController) {
            context.hideBottomNavigation()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSinglePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDetach() {
        super.onDetach()
        if (context is BottomNavigationController) {
            (context as BottomNavigationController).showBottomNavigation()
        }
    }

    override fun onResume() {
        super.onResume()
        if (context is BottomNavigationController) {
            (context as BottomNavigationController).hideBottomNavigation()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlist = arguments?.getParcelable(PlaylistsFragment.PLAYLIST_KEY)

        initAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        val bottomSheetContainer = binding.llBottomSheet

        val bottomSheetBehavior: BottomSheetBehavior<LinearLayout> =
            BottomSheetBehavior.from(bottomSheetContainer).apply {
                isHideable = false
                state = BottomSheetBehavior.STATE_COLLAPSED
            }

        bottomSheetObserver(bottomSheetBehavior, binding.overlay)
        fetchPalylist()
        listeners()
        viewModelObserver()
        viewModel.getTracks(playlist!!.id)

        binding.ivShare.post {
            val location = IntArray(2)
            binding.ivShare.getLocationInWindow(location)
            val shareBottom = location[1] + binding.ivShare.height
            val screenHeight = Resources.getSystem().displayMetrics.heightPixels
            val peekHeight = screenHeight - shareBottom + 40

            bottomSheetBehavior.peekHeight = peekHeight
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

    private fun fetchPalylist() {
        with(binding) {
            Glide.with(this@SinglePlaylist)
                .load(playlist?.imagePath)
                .placeholder(R.drawable.ic_cover_ph)
                .transform(CenterCrop())
                .into(ivCoverPh)
            tvTitle.text = playlist?.title
            tvDescription.text = playlist?.description
        }
    }

    private fun initAdapter() {
        adapter = TrackAdapter(
            playlistId = playlist?.id ?: 0,
            onClickedTrack = { track ->
                if (viewModel.clickDebounce()) {
                    val bundle = Bundle().apply {
                        putParcelable(SearchFragment.TRACK_KEY, track)
                    }
                    findNavController().navigate(R.id.action_global_to_playerFragment, bundle)
                }
            },
            onDeleteTrack = { _, track ->
                onDeleteTrack(track)
            }
        )
    }


    private fun listeners() {
        binding.ivArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.ivShare.setOnClickListener {
            if (trackCount == 0) {
                Tools.vibroManager(requireContext(), 50)
                Tools.showSnackbar(
                    binding.root,
                    getString(R.string.empty_playlist),
                    requireContext()
                )
            } else {
                viewModel.sharePlaylist(playlist!!, tracks)
            }
        }
    }

    private fun onDeleteTrack(track: Track) {
        viewModel.removeTrackFromPlaylist(playlist!!, track)
        viewModel.getTracks(playlist!!.id)
    }

    private fun viewModelObserver() {
        viewModel.playlistState.observe(viewLifecycleOwner) {
            if (it is PlaylistTracksState.Content) {
                adapter.trackList.clear()
                adapter.trackList.addAll(it.trackList)
                viewModel.calculatePlaylistDuration(it.trackList)
                trackCount = it.trackList.size
                tracks = it.trackList.toMutableList()
                viewModel.calculatetracksNumber(trackCount)
                adapter.notifyDataSetChanged()
            } else {
                adapter.trackList.clear()
                viewModel.calculatetracksNumber(0)
                viewModel.calculatePlaylistDuration(emptyList())
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.playlistDuration.observe(viewLifecycleOwner) {
            binding.tvDuration.text = it
        }
        viewModel.tracksNumber.observe(viewLifecycleOwner) {
            binding.tvAmount.text = it
        }
    }
}
