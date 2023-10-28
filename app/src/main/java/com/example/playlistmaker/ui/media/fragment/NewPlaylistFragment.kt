package com.example.playlistmaker.ui.media.fragment

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorRes
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.domain.media.models.NewPlaylistState
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.ui.media.view_model.NewPlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class NewPlaylistFragment : Fragment() {
    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewPlaylistViewModel by viewModel()

    private var imagePath: Uri? = null

    private val pickMedia = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val cornerRadius = requireContext().resources.getDimensionPixelSize(R.dimen.dimen_8dp)

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
            val playlist = Playlist(
                0,
                title = binding.titleEdit.text.toString(),
                description = binding.descriptionEdit.text.toString(),
                imagePath = imagePath,
                trackIds = "",
                trackAmount = 0
            )
            viewModel.savePlayList(playlist)

            Toast.makeText(
                requireContext(),
                "Плейлист ${playlist.title} создан",
                Toast.LENGTH_SHORT
            ).show()

            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.buttonCover.setOnClickListener {
            GlobalScope.launch {
                val colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), Color.RED, Color.GREEN)
                colorAnimator.duration = 1500
                colorAnimator.addUpdateListener { animator ->
                    val color = animator.animatedValue as Int
                    binding.titleInput.boxStrokeColor = color
                }
                colorAnimator.start()
                delay(1500)
            }
        }

        binding.titleEdit.doOnTextChanged() { text, _, _, _ ->
            if (text.isNullOrEmpty()) {
                viewModel.setEmptyState()
                Log.e("NewPlaylistFragment", "EditTextFilled")
            }
            else {
                viewModel.setNotEmptyState()
                Log.e("NewPlaylistFragment", "EditText mpt")
            }
        }
    }

    private fun obserViewModel() {
        viewModel.state.observe(viewLifecycleOwner) {
            if (it is NewPlaylistState.Empty) renderUi(false, buttonColor = R.color.light_gray)
            else renderUi(true, buttonColor = R.color.blue_text)
        }
    }

    private fun renderUi(isButtonEnabled: Boolean, @ColorRes buttonColor: Int) {
        binding.createButton.isEnabled = isButtonEnabled
        binding.buttonCover.isEnabled = !isButtonEnabled
        binding.createButton.setBackgroundColor(resources.getColor(buttonColor, null))
    }

    private fun saveImageToInternalStorage(uri: Uri) {
        val filePath = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            getString(R.string.playlists)
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, uri.lastPathSegment ?: "image")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

        imagePath = file.toUri()
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Покинуть экран создания плейлиста?")
            .setMessage("Введённые данные будут утрачены")
            .setNeutralButton("Отмена") { _, _ -> }
            .setNegativeButton("Покинуть") { _, _ ->
                requireActivity().supportFragmentManager.popBackStack()
            }
            .show()
    }

    companion object {
        fun newInstance() = NewPlaylistFragment()
    }
}
