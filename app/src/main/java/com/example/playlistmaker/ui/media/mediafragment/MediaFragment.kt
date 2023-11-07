package com.example.playlistmaker.ui.media.mediafragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaBinding
import com.example.playlistmaker.ui.media.mediafragment.adapter.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MediaFragment : Fragment() {
    private var _binding: FragmentMediaBinding? = null
    private val binding get() = _binding!!

    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = binding.viewPager
        viewPager.adapter =
            FragmentPagerAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(
            binding.tabLayout, viewPager
        ) { tab, position ->
            when (position) {
                0 -> tab.text = this.getString(R.string.favorite_tracks)
                1 -> tab.text = this.getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        if (::tabMediator.isInitialized) {
            tabMediator.detach()
        }
        _binding = null
        super.onDestroy()
    }
}
