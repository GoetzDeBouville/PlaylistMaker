package com.example.playlistmaker.ui.search.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemTrackBinding
import com.example.playlistmaker.domain.search.models.Track

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

    var trackList = ArrayList<Track>()
    override fun getItemCount() = trackList.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])

        holder.itemView.setOnClickListener {
            onClickedTrack?.invoke(trackList[position])
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
