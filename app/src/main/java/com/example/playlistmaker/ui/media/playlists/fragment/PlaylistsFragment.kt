package com.example.playlistmaker.ui.media.playlists.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.media.models.PlaylistState
import com.example.playlistmaker.ui.media.playlists.adapter.GridSpacingItemDecoration
import com.example.playlistmaker.ui.media.playlists.viewmodel.PlaylistsViewModel
import com.example.playlistmaker.ui.media.playlists.adapter.PlaylistsAdapter
import com.example.playlistmaker.utils.Tools
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistsViewModel by viewModel()
    private var adapter = PlaylistsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addPlaylistBtn.setOnClickListener {
            findNavController().navigate(R.id.action_global_to_newPlaylistFragment)
        }

        viewModel.getPlaylists()
        initAdapters()
        viewModel.state.observe(viewLifecycleOwner) {
            if(it is PlaylistState.Content) {
                binding.ivPlaceholder.visibility = View.GONE
                binding.tvPlaceholderMessage.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                adapter.playlists.clear()
                adapter.playlists.addAll(it.playlists)
                adapter.notifyDataSetChanged()
            } else {
                binding.ivPlaceholder.visibility = View.VISIBLE
                binding.tvPlaceholderMessage.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
        }

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(GridSpacingItemDecoration(2, resources.getDimensionPixelSize(R.dimen.dimen_8dp), true))
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylists()
    }

    private fun initAdapters() {
        adapter = PlaylistsAdapter {
            if(viewModel.clickDebounce()) {
                val bundle = Bundle().apply {
                    putParcelable(Tools.PLAYLIST_DATA, it)
                }
                findNavController().navigate(R.id.action_global_to_singlePlaylist, bundle)
            }
        }
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}
