package com.example.playlistmaker.ui.media.fragment

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
import com.example.playlistmaker.ui.media.adapters.PlaylistsAdapter
import com.example.playlistmaker.ui.media.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistsViewModel by viewModel()
    private val adapter = PlaylistsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addPlaylistBtn.setOnClickListener {
            findNavController().navigate(R.id.action_global_to_newPlaylistFragment)
        }

        viewModel.getPlaylists()

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
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylists()
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}
