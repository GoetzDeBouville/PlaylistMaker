package com.example.playlistmaker.ui.media.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.media.models.PlaylistState
import com.example.playlistmaker.ui.media.adapters.PlaylistsAdapter
import com.example.playlistmaker.ui.media.view_model.PlaylistsViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
// todo требуется реализзовать снэкбар
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addPlaylistBtn.setOnClickListener {
            val newPlaylistFragment = NewPlaylistFragment.newInstance()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(android.R.id.content, newPlaylistFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.ll.setOnClickListener {
            showCustomSnackbar(binding.root)
        }

        viewModel.getPlaylists()

        viewModel.state.observe(viewLifecycleOwner) {
            if(it is PlaylistState.Content) {
                binding.placeholderImage.visibility = View.GONE
                binding.placeholderMessage.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                Log.i("PlaylistsFragment", "StateContent = ${it.playlists}")
                adapter.playlists.clear()
                adapter.playlists.addAll(it.playlists)
                adapter.notifyDataSetChanged()
            } else {
                Log.i("PlaylistsFragment", "StateContent = Empty")
                binding.placeholderImage.visibility = View.VISIBLE
                binding.placeholderMessage.visibility = View.VISIBLE
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


    private fun showCustomSnackbar(rootView: View) {
        val snackbarLayout = LayoutInflater.from(rootView.context).inflate(R.layout.item_snackbar, null)
        val container = rootView.findViewById<ViewGroup>(R.id.snackbar_container)

        container.addView(snackbarLayout)
        snackbarLayout.animation = AnimationUtils.loadAnimation(rootView.context, R.anim.snackbar_enter)
        snackbarLayout.visibility = View.VISIBLE

        GlobalScope.launch {
            val exitAnimation = AnimationUtils.loadAnimation(rootView.context, R.anim.snackbar_exit)
            exitAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    container.removeView(snackbarLayout)
                }
            })
            snackbarLayout.startAnimation(exitAnimation)
            delay(2000)
        }
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}
