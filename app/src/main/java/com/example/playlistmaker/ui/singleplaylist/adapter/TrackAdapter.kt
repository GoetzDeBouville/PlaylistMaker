package com.example.playlistmaker.ui.singleplaylist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemTrackBinding
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.utils.Tools
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TrackAdapter(
    private val playlist: Playlist,
    private var onClickedTrack: ((Track) -> Unit)? = null,
    private val onDeleteTrack: ((Int, Track) -> Unit)? = null,
    private val onUpdatePlaylist: ((Playlist) -> Unit)? = null
) :
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

    var trackList = ArrayList<Track>()
    override fun getItemCount() = trackList.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])

        holder.itemView.setOnClickListener {
            onClickedTrack?.invoke(trackList[position])
        }

        holder.itemView.setOnLongClickListener {
            Tools.vibroManager(it.context, 50)
            MaterialAlertDialogBuilder(it.context)
                .setTitle(R.string.confirming_deletion_track)
                .setMessage("")
                .setPositiveButton(R.string.delete) { _, _ ->
                    onDeleteTrack?.invoke(playlist.id, trackList[position])
                    onUpdatePlaylist?.invoke(playlist)
                }
                .setNegativeButton(R.string.cancel) { _, id ->
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
