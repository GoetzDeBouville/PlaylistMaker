package com.example.playlistmaker.ui.media.editplaylist.fragment

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.ui.media.editplaylist.viewmodel.EditPlaylistViewModel
import com.example.playlistmaker.ui.media.newplaylist.fragment.NewPlaylistFragment
import com.example.playlistmaker.utils.Tools
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EditPlaylistFragment : NewPlaylistFragment() {
    private val viewModel: EditPlaylistViewModel by viewModel()
    private var playlist: Playlist? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlist = arguments?.getParcelable(Tools.PLAYLIST_DATA)
        imagePath = playlist?.imagePath
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        playlist?.let { fetchFragment(it) }
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

            if (binding.cvCreateButton.cardBackgroundColor.defaultColor == blueButton) {
                playlist?.let {
                    val updatedPlaylist = Playlist(
                        id = it.id,
                        title = binding.etTitle.text.toString(),
                        description = binding.etDescription.text.toString(),
                        imagePath = imagePath,
                        trackAmount = it.trackAmount
                    )
                    viewModel.updatePlaylist(updatedPlaylist)

                    findNavController().popBackStack()
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.action_global_to_singlePlaylist, bundleOf(Tools.PLAYLIST_DATA to updatedPlaylist))
                } ?: return@setOnClickListener
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
            val cornerRadius = resources.getDimensionPixelSize(R.dimen.dimen_8dp)
            Glide.with(this@EditPlaylistFragment)
                .load(playlist.imagePath)
                .placeholder(R.drawable.ic_component_ph)
                .transform(CenterCrop(), RoundedCorners(cornerRadius))
                .into(ivPlCover)
            etTitle.setText(playlist.title)
            etDescription.setText(playlist.description)
        }
    }
}
