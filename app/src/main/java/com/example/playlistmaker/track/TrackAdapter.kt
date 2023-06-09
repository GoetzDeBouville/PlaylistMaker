package com.example.playlistmaker.track

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R

class TrackAdapter : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {
    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val songTitle: TextView = itemView.findViewById(R.id.track_name)
        private val artist: TextView = itemView.findViewById(R.id.artist_name)
        private val duration: TextView = itemView.findViewById(R.id.track_time)
        private val albumCover: ImageView = itemView.findViewById(R.id.album_poster_image)

        fun bind(model: Track) {
            songTitle.text = model.trackName
            artist.text = model.artistName
            duration.text = model.timeFormater()
            Glide.with(itemView)
                .load(model.artworkUrl100)
                .placeholder(R.drawable.empty_poster)
                .transform(RoundedCorners(10))
                .into(albumCover)
        }
    }

    var trackList = ArrayList<Track>()
    var onClickedTrack: ((Track) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount() = trackList.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])

        holder.itemView.setOnClickListener {
            onClickedTrack?.invoke(trackList[position])
        }
    }
}