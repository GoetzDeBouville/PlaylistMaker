package com.example.playlistmaker.ui.player.adapter

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
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {
    class PlaylistViewHolder(private val binding: ItemPlaylistLinearBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(playlist: Playlist) {
            binding.tvTitle.text = playlist.title
            binding.tvAmount.text = Tools.amountTextFormater(playlist.trackAmount)
            Glide.with(itemView)
                .load(playlist.imagePath)
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

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onClicked?.invoke(playlists[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = ItemPlaylistLinearBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaylistViewHolder(binding)
    }
}
