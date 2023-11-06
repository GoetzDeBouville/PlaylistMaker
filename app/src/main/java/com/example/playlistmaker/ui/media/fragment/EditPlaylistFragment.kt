package com.example.playlistmaker.ui.media.fragment

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.ui.media.view_model.EditPlaylistViewModel
import com.example.playlistmaker.utils.Tools
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EditPlaylistFragment : NewPlaylistFragment() {
    private val binding get() = _bindingPl!!
    private val viewModel: EditPlaylistViewModel by viewModel()
    private var playlist: Playlist? = null

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingPl = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlist = arguments?.getParcelable(Tools.PLAYLIST_DATA)
        imagePath = playlist?.imagePath
        Log.i("EditPlaylistFragment", "playlist = $playlist")
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        fetchFragment(playlist!!)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        actionListeners()
    }

    override fun actionListeners() {
        super.actionListeners()

        binding.ivArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.cvCreateButton.setOnClickListener {
            val blueButton = ContextCompat.getColor(requireContext(), R.color.blue_text)

            Log.i("EditPlaylistFragment", "imagePath = $imagePath")
            if (binding.cvCreateButton.cardBackgroundColor.defaultColor == blueButton) {
                val playlist = Playlist(
                    playlist!!.id,
                    title = binding.etTitle.text.toString(),
                    description = binding.etDescription.text.toString(),
                    imagePath = imagePath,
                    trackAmount = playlist!!.trackAmount
                )
                viewModel.updatePlaylist(playlist)

                findNavController().popBackStack()
                findNavController().popBackStack()
                findNavController().navigate(R.id.singlePlaylist, bundleOf(Tools.PLAYLIST_DATA to playlist))
            } else {
                viewLifecycleOwner.lifecycleScope.launch {
                    val colorAnimator =
                        ValueAnimator.ofObject(ArgbEvaluator(), Color.RED, Color.TRANSPARENT)
                    colorAnimator.duration = 300
                    colorAnimator.repeatMode = ValueAnimator.REVERSE
                    colorAnimator.repeatCount = 3
                    colorAnimator.addUpdateListener { animator ->
                        val color = animator.animatedValue as Int
                        binding.tiTitle.boxStrokeColor = color
                        binding.tiTitle.boxBackgroundColor = color
                    }
                    colorAnimator.start()

                    delay(1000)
                    colorAnimator.cancel()

                    binding.tiTitle.boxStrokeColor =
                        ContextCompat.getColor(requireContext(), R.color.blue_text)
                    binding.tiTitle.boxBackgroundColor =
                        ContextCompat.getColor(requireContext(), R.color.transparent)
                }
            }
        }
    }

    private fun fetchFragment(playlist: Playlist) {
        binding.tvBtnText.text = getString(R.string.save)
        binding.tvHeaderTitle.text = getString(R.string.edit)
        with(binding) {
            Glide.with(this@EditPlaylistFragment)
                .load(playlist.imagePath)
                .placeholder(R.drawable.ic_cover_ph)
                .transform(CenterCrop())
                .into(ivPlCover)
            etTitle.setText(playlist.title)
            etDescription.setText(playlist.description)
        }
    }
}
