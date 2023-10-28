package com.example.playlistmaker.ui.media.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemPlaylistBinding
import com.example.playlistmaker.domain.media.models.Playlist

class PlaylistsAdapter(private var onClickedTrack: ((Playlist) -> Unit)? = null) :
    RecyclerView.Adapter<PlaylistsAdapter.PlailistViewHolder>() {
    class PlailistViewHolder(private val binding: ItemPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(plailist: Playlist) {
            binding.tvTitle.text = plailist.title
            binding.tvAmount.text = amountTextFormater(plailist.trackAmount)
            Glide.with(itemView)
                .load(plailist.imagePath)
                .placeholder(R.drawable.empty_poster)
                .transform(MultiTransformation(CenterCrop(), RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.dimen_8dp))))
                .into(binding.imgCover)
        }

        private fun amountTextFormater(amount: Int): String {
            val lastDigit = amount % 10
            val lastTwoDigits = amount % 100

            return when {
                lastTwoDigits in 11..14 -> "$amount треков"
                lastDigit == 1 -> "$amount трек"
                lastDigit in 2..4 -> "$amount трека"
                else -> "$amount треков"
            }
        }
    }

    var playlists = ArrayList<Playlist>()
    override fun getItemCount() = playlists.size

    override fun onBindViewHolder(holder: PlailistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            Log.i(
                "PLAdapter",
                "Clicked on pl title = ${playlists[position].title} description = ${playlists[position].description} trackAmount = ${playlists[position].trackAmount}"
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlailistViewHolder {
        Log.i(
            "PLAdapter",
            "onCreateViewHolder"
        )
        val binding = ItemPlaylistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlailistViewHolder(binding)
    }
}
