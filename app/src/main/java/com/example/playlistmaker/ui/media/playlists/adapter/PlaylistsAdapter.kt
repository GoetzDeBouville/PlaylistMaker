package com.example.playlistmaker.ui.media.playlists.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemPlaylistGridBinding
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.utils.Tools

class PlaylistsAdapter(private var onClicked: ((Playlist) -> Unit)? = null) :
    RecyclerView.Adapter<PlaylistsAdapter.PlaylistViewHolder>() {
    class PlaylistViewHolder(private val binding: ItemPlaylistGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(playlist: Playlist) = with(binding){
            tvTitle.text = playlist.title
            tvAmount.text = Tools.amountTextFormater(playlist.trackAmount)
            ivCover.load(playlist.imagePath) {
                placeholder(R.drawable.ic_cover_ph)
                transformations(RoundedCornersTransformation(
                    radius = itemView.resources.getDimensionPixelSize(R.dimen.dimen_8dp).toFloat()
                ))
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
        val binding = ItemPlaylistGridBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaylistViewHolder(binding)
    }
}
