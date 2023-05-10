package com.example.playlistmaker.track

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackImage: ImageView = itemView.findViewById(R.id.album_poster_image)
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val groupName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)

    fun bind(item: Track) {
        val adaptedTrackName = item.trackName
        val adaptedArtistName = item.artistName
        val adaptedTrackTime = item.trackTime

        Glide.with(itemView)
            .load(item.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.empty_poster)
            .into(trackImage)

        trackName.text = adaptedTrackName
        groupName.text = adaptedArtistName
        trackTime.text = adaptedTrackTime
    }
}