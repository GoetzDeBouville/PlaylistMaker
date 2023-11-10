package com.example.playlistmaker.ui.media.playlists.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemPlaylistGridBinding
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.utils.Tools

class PlaylistsAdapter(private var onClicked: ((Playlist) -> Unit)? = null) :
    RecyclerView.Adapter<PlaylistsAdapter.PlaylistViewHolder>() {
    class PlaylistViewHolder(private val binding: ItemPlaylistGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(playlist: Playlist) {
            binding.tvTitle.text = playlist.title
            binding.tvAmount.text = Tools.amountTextFormater(playlist.trackAmount)
            Glide.with(itemView)
                .load(playlist.imagePath)
                .placeholder(R.drawable.ic_cover_ph)
                .transform(MultiTransformation(CenterCrop(), RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.dimen_8dp))))
                .into(binding.ivCover)
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
        val binding = ItemPlaylistGridBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaylistViewHolder(binding)
    }
}
