package com.example.playlistmaker.ui.singleplaylist

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSinglePlaylistBinding
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.ui.main.BottomNavigationController
import com.example.playlistmaker.ui.media.fragment.PlaylistsFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior

class SinglePlaylist : Fragment() {
    private var _binding: FragmentSinglePlaylistBinding? = null
    private val binding get() = _binding!!
    private var playlist: Playlist? = null
    private var adapter = TrackAdapter()

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
        playlist = null
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

        val bottomSheetContainer = binding.llBottomSheet
        val bottomSheetBehavior: BottomSheetBehavior<LinearLayout> =
            BottomSheetBehavior.from(bottomSheetContainer).apply {
                state = BottomSheetBehavior.STATE_COLLAPSED
            }
        bottomSheetObserver(bottomSheetBehavior, binding.overlay)
        fetchPalylist()
        listeners()
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
            tvDuration.text = "duration"
            tvAmount.text = playlist?.trackAmount.toString()
        }
    }

    private fun listeners() {
        binding.ivArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
