package com.example.playlistmaker.ui.media.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.domain.media.models.FavoriteTracksState
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.media.view_model.FavoriteTracksViewModel
import com.example.playlistmaker.ui.search.adapters.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {
    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteTracksViewModel by viewModel()
    private lateinit var adapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        viewModel.state.observe(viewLifecycleOwner) {
            renderState(it)
        }
        initRecyclerView()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoriteTracks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAdapters() {
        if (_binding != null) {
            adapter = TrackAdapter {
                if (viewModel.clickDebounce()) {
                    val bundle = Bundle().apply {
                        putParcelable("track", it)
                    }
                    findNavController().navigate(R.id.action_global_to_playerFragment, bundle)
                }
            }
            binding.rvFavouriteTracks.adapter = adapter
        }
    }

    private fun initRecyclerView() {
        initAdapters()
        binding.rvFavouriteTracks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavouriteTracks.adapter = adapter
        binding.rvFavouriteTracks.itemAnimator = null
    }

    private fun renderState(state: FavoriteTracksState) {
        when (state) {
            is FavoriteTracksState.Content -> showTracks(state.favoriteTracks)
            is FavoriteTracksState.Empty -> showEmpty()
        }
    }

    private fun showEmpty() {
        binding.llPlaceholder.visibility = View.VISIBLE
        binding.favouriteTrackList.visibility = View.INVISIBLE
    }

    private fun showTracks(favoriteTracksList: List<Track>) {
        binding.llPlaceholder.visibility = View.GONE
        binding.favouriteTrackList.visibility = View.VISIBLE
        adapter.trackList = ArrayList(favoriteTracksList)
        adapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }
}
