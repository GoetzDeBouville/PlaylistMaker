package com.example.playlistmaker.ui.singleplaylist.fragment

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSinglePlaylistBinding
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.domain.media.models.PlaylistTracksState
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.search.fragment.SearchFragment
import com.example.playlistmaker.ui.singleplaylist.viewmodel.SinglePlaylistViewModel
import com.example.playlistmaker.ui.singleplaylist.adapter.TrackAdapter
import com.example.playlistmaker.utils.Tools
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class SinglePlaylistFragment : Fragment() {
    private var _binding: FragmentSinglePlaylistBinding? = null
    private val binding get() = _binding!!
    private var playlist: Playlist? = null
    private val viewModel: SinglePlaylistViewModel by viewModel()
    private var trackCount: Int = 0
    private var tracks: List<Track> = emptyList()
    private lateinit var adapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSinglePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlist = arguments?.getParcelable(Tools.PLAYLIST_DATA)

        initAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        val bottomSheetContainer = binding.llBottomSheet

        val bottomSheetTracksContainer: BottomSheetBehavior<LinearLayout> =
            BottomSheetBehavior.from(bottomSheetContainer).apply {
                state = BottomSheetBehavior.STATE_COLLAPSED
            }
        bottomSheetObserver(bottomSheetTracksContainer, binding.overlay)

        val bottomSheetMenuContainer = binding.llBottomSheetMenu

        val bottomSheetMenu: BottomSheetBehavior<LinearLayout> =
            BottomSheetBehavior.from(bottomSheetMenuContainer).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        bottomSheetMenu.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetObserver(bottomSheetMenu, binding.overlay)

        fetchPalylist()
        listeners()
        viewModelObserver()
        viewModel.getTracks(playlist!!.id)

        binding.ivShare.post {
            bottomSheetTracksContainer.peekHeight = calcHeight(binding.ivShare)
        }

        binding.tvTitle.post {
            bottomSheetMenu.peekHeight = calcHeight(binding.tvTitle)
        }

        binding.ivActionBtn.setOnClickListener {
            bottomSheetMenu.state = BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetTracksContainer.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    override fun onResume() {
        super.onResume()
        fetchPalylist()
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

    private fun calcHeight(view: View) : Int {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val viewHeight = location[1] + view.height
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        val extraSpacing = resources.getDimensionPixelSize(R.dimen.dimen_12dp)

        return (screenHeight - viewHeight - extraSpacing)
    }

    private fun fetchPalylist() {
        with(binding) {

            ivCoverPh.load(playlist?.imagePath) {
                placeholder(R.drawable.ic_cover_ph)
                transformations(RoundedCornersTransformation())
            }
            tvTitle.text = playlist?.title
            tvDescription.text = playlist?.description

            val cornerRadius =
                requireContext().resources.getDimensionPixelSize(R.dimen.dimen_8dp)

            ivPlCover.load(playlist?.imagePath) {
                placeholder(R.drawable.empty_poster)
                transformations(RoundedCornersTransformation(cornerRadius.toFloat()))
            }
            tvBsMenuTitle.text = getString(R.string.playlist_title_description, playlist?.title, playlist?.description)
        }
    }

    private fun initAdapter() {
        adapter = TrackAdapter(
            playlist = playlist!!,
            onClickedTrack = { track ->
                if (viewModel.clickDebounce()) {
                    val gson = Gson()
                    val trackJson = gson.toJson(track)
                    val bundle = Bundle().apply {
                        putString(SearchFragment.TRACK_KEY, trackJson)
                    }
                    findNavController().navigate(R.id.action_global_to_playerFragment, bundle)
                }
            },
            onDeleteTrack = { _, track ->
                onDeleteTrack(track)
            },
            onUpdatePlaylist = { playlist ->
                updatePlaylist(playlist)
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
        binding.tvShare.setOnClickListener {
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

        binding.tvDeletePlaylist.setOnClickListener {
            MaterialAlertDialogBuilder(it.context)
                .setTitle(getString(R.string.confirming_deletion_playlist, playlist!!.title))
                .setMessage("")
                .setPositiveButton(getString(R.string.delete)) { _, _ ->
                    viewModel.removePlaylist(playlist!!)
                    findNavController().navigateUp()
                }
                .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                }
                .create()
                .show()
        }

        binding.tvEditInfo.setOnClickListener {
            findNavController().navigate(
                R.id.action_singlePlaylist_to_editPlaylistFragment,
                bundleOf(Tools.PLAYLIST_DATA to playlist)
            )
        }
    }

    private fun onDeleteTrack(track: Track) {
        viewModel.removeTrackFromPlaylist(playlist!!, track)
        viewModel.getTracks(playlist!!.id)
    }

    private fun updatePlaylist(playlist: Playlist) {
        viewModel.updatePlaylist(playlist)
    }

    private fun viewModelObserver() {
        viewModel.playlistState.observe(viewLifecycleOwner) {
            if (it is PlaylistTracksState.Content) {
                adapter.trackList.clear()
                adapter.trackList.addAll(it.trackList)
                viewModel.calculatePlaylistDuration(it.trackList)
                trackCount = it.trackList.size
                tracks = it.trackList.toMutableList()
                viewModel.calculateTracksNumber(trackCount)
                adapter.notifyDataSetChanged()
            } else {
                adapter.trackList.clear()
                viewModel.calculateTracksNumber(0)
                trackCount = 0
                viewModel.calculatePlaylistDuration(emptyList())
                adapter.notifyDataSetChanged()
                Tools.showSnackbar(
                    binding.root,
                    getString(R.string.empty_playlist_toast),
                    requireContext()
                )
            }
        }

        viewModel.playlistDuration.observe(viewLifecycleOwner) {
            binding.tvDuration.text = it
        }
        viewModel.tracksNumber.observe(viewLifecycleOwner) {
            binding.tvAmount.text = it
            binding.tvBsMenuAmount.text = it
        }
    }
}
