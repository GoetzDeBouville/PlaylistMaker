package com.example.playlistmaker.ui.player.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemPlaylistLinearBinding
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.utils.Tools

class PlaylistAdapter(private var onClicked: ((Playlist) -> Unit)? = null) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {
    class PlaylistViewHolder(private val binding: ItemPlaylistLinearBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(playlist: Playlist) = with(binding) {
            tvTitle.text = playlist.title
            tvAmount.text = Tools.amountTextFormater(playlist.trackAmount)
            ivCover.load(playlist.imagePath) {
                placeholder(R.drawable.empty_poster)
                transformations(
                    RoundedCornersTransformation(
                        radius = itemView.resources.getDimensionPixelSize(R.dimen.dimen_8dp)
                            .toFloat()
                    )
                )
            }
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
