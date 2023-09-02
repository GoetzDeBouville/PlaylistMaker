package com.example.playlistmaker.ui.media

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.media.view_model.FavoriteTracksViewModel

class FavoriteTracksFragment : Fragment() {
    private lateinit var viewModel: FavoriteTracksViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite_tracks, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FavoriteTracksViewModel::class.java)
        // TODO: Use the ViewModel
    }
    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }
}
