package com.example.playlistmaker.ui.singleplaylist

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemTrackBinding
import com.example.playlistmaker.domain.search.models.Track
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TrackAdapter(private var onClickedTrack: ((Track) -> Unit)? = null) :
    RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {
    class TrackViewHolder(private val binding: ItemTrackBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Track) {
            with(binding) {
                trackName.text = model.trackName
                artistName.text = model.artistName
                trackTime.text = model.timeFormater()
                Glide.with(itemView)
                    .load(model.artworkUrl100)
                    .placeholder(R.drawable.empty_poster)
                    .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.dimen_8dp)))
                    .into(albumPosterImage)
            }
        }
    }
    private var vibrator: Vibrator? = null
    var trackList = ArrayList<Track>()
    override fun getItemCount() = trackList.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])

        holder.itemView.setOnClickListener {
            onClickedTrack?.invoke(trackList[position])
        }

        holder.itemView.setOnLongClickListener {
            val context = it.context
            vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            val vibrationEffect = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                VibrationEffect.createOneShot(
                    50,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            } else {
                VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)
            }
            vibrator?.vibrate(vibrationEffect)
            MaterialAlertDialogBuilder(it.context)
                .setTitle("Хотите удалить трек?") // Заголовок диалога
                .setPositiveButton("Да") { dialog, id ->
                    // Обработка нажатия на кнопку "Да"
                }
                .setNegativeButton("Нет") { dialog, id ->
                    // Обработка нажатия на кнопку "Нет"
                }
                .create()
                .show()
            true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = ItemTrackBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TrackViewHolder(binding)
    }

}
