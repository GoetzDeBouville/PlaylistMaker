package com.example.playlistmaker.ui.player.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemPlaylistLinearBinding
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.utils.Tools

class PlaylistAdapter(private var onClicked: ((Playlist) -> Unit)? = null) :
    RecyclerView.Adapter<PlaylistAdapter.PlailistViewHolder>() {
    class PlailistViewHolder(private val binding: ItemPlaylistLinearBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(plailist: Playlist) {
            binding.tvTitle.text = plailist.title
            binding.tvAmount.text = Tools.amountTextFormater(plailist.trackAmount)
            Glide.with(itemView)
                .load(plailist.imagePath)
                .placeholder(R.drawable.empty_poster)
                .transform(
                    MultiTransformation(
                        CenterCrop(), RoundedCorners(
                            itemView.resources.getDimensionPixelSize(
                                R.dimen.dimen_8dp
                            )
                        )
                    )
                )
                .into(binding.imgCover)
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
            onClicked?.invoke(playlists[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlailistViewHolder {
        Log.i(
            "PLAdapter",
            "onCreateViewHolder"
        )
        val binding = ItemPlaylistLinearBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlailistViewHolder(binding)
    }
}
