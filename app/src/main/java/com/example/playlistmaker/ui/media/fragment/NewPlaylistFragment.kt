package com.example.playlistmaker.ui.media.fragment

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.domain.media.models.NewPlaylistState
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.ui.media.view_model.NewPlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class NewPlaylistFragment : Fragment() {
    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewPlaylistViewModel by viewModel()

    private var imagePath: Uri? = null

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val cornerRadius =
                    requireContext().resources.getDimensionPixelSize(R.dimen.dimen_8dp)

                Glide.with(requireContext())
                    .load(uri)
                    .transform(CenterCrop(), RoundedCorners(cornerRadius))
                    .into(binding.plImage)

                saveImageToInternalStorage(uri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showDialog()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        obserViewModel()
        actionListeners()
    }

    private fun actionListeners() {
        binding.arrowBack.setOnClickListener {
            showDialog()
        }

        binding.plImage.setOnClickListener {
            pickMedia.launch("image/*")
        }

        binding.createButton.setOnClickListener {
            val blueButton = ContextCompat.getColor(requireContext(), R.color.blue_text)

            if (binding.createButton.cardBackgroundColor.defaultColor == blueButton) {
                val playlist = Playlist(
                    0,
                    title = binding.titleEdit.text.toString(),
                    description = binding.descriptionEdit.text.toString(),
                    imagePath = imagePath,
                    trackIds = "",
                    trackAmount = 0
                )
                viewModel.savePlayList(playlist)

                showSnackbar(playlist.title)
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                viewLifecycleOwner.lifecycleScope.launch {
                    val colorAnimator =
                        ValueAnimator.ofObject(ArgbEvaluator(), Color.RED, Color.TRANSPARENT)
                    colorAnimator.duration = 300
                    colorAnimator.repeatMode = ValueAnimator.REVERSE
                    colorAnimator.repeatCount = 3
                    colorAnimator.addUpdateListener { animator ->
                        val color = animator.animatedValue as Int
                        binding.titleInput.boxStrokeColor = color
                        binding.titleInput.boxBackgroundColor = color
                    }
                    colorAnimator.start()

                    delay(1000)
                    colorAnimator.cancel()

                    binding.titleInput.boxStrokeColor =
                        ContextCompat.getColor(requireContext(), R.color.blue_text)
                    binding.titleInput.boxBackgroundColor =
                        ContextCompat.getColor(requireContext(), R.color.transparent)
                }
            }
        }

        binding.titleEdit.doOnTextChanged() { text, _, _, _ ->
            if (text.isNullOrEmpty()) viewModel.setEmptyState()
            else viewModel.setNotEmptyState()
        }
    }

    private fun obserViewModel() {
        viewModel.state.observe(viewLifecycleOwner) {
            if (it is NewPlaylistState.Empty) renderUi(buttonColor = R.color.text_gray)
            else renderUi(buttonColor = R.color.blue_text)
        }
    }

    private fun renderUi(@ColorRes buttonColor: Int) {
        binding.createButton.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                buttonColor
            )
        )
    }

    private fun saveImageToInternalStorage(uri: Uri) {
        val filePath = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            getString(R.string.covers)
        )

        if (!filePath.exists()) filePath.mkdirs()

        val file = File(filePath, uri.lastPathSegment ?: "image")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

        imagePath = file.toUri()
    }

    private fun showDialog() {
        if (binding.titleEdit.text.toString()
                .isNotEmpty() || binding.descriptionEdit.text.toString()
                .isNotEmpty() || (imagePath != null)
        ) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.dialog_question)
                .setMessage(R.string.losing_data)
                .setNeutralButton(R.string.cancel) { _, _ -> }
                .setNegativeButton(R.string.done) { _, _ ->
                    requireActivity().supportFragmentManager.popBackStack()
                }
                .show()
        } else {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
    private fun showSnackbar(title : String) {
        val snackbar = Snackbar.make(binding.root, "Плейлист $title создан", Snackbar.LENGTH_SHORT)
        val snackTextColor = ContextCompat.getColor(requireContext(), R.color.snack_text)
        val backgroundColor = ContextCompat.getColor(requireContext(), R.color.text_color)

        val textView =
            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        textView.textSize = 16f
        textView.setTextColor(snackTextColor)
        snackbar.view.setBackgroundColor(backgroundColor)
        snackbar.show()
    }

    companion object {
        fun newInstance() = NewPlaylistFragment()
    }
}
