package com.example.playlistmaker.ui.media.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.media.adapters.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MediaFragment : Fragment() {

    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_media, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        viewPager.adapter =
            FragmentPagerAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(
            view.findViewById(R.id.tabLayout),
            viewPager
        ) { tab, position ->
            when (position) {
                0 -> tab.text = this.getString(R.string.favorite_tracks)
                1 -> tab.text = this.getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}