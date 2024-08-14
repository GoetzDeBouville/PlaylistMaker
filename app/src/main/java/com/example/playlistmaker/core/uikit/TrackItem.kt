package com.example.playlistmaker.core.uikit

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.example.playlistmaker.R
import com.example.playlistmaker.core.uikit.icons.ArrowForwardImg
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.theme.AppTheme
import com.example.playlistmaker.utils.extensions.toMinutes

@Composable
fun TrackItem(
    modifier: Modifier = Modifier,
    track: Track
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val context = LocalContext.current
        val imageLoader = context.imageLoader

        AsyncImage(
            alignment = Alignment.Center,
            model = ImageRequest.Builder(LocalContext.current)
                .data(track.artworkUrl100)
                .crossfade(true)
                .error(context.getDrawable(R.drawable.empty_poster))
                .placeholder(context.getDrawable(R.drawable.empty_poster))
                .build(),
            contentDescription = "Album cover ${track.artistName} ${track.collectionName}",
            imageLoader = imageLoader,

            modifier = modifier
                .size(45.dp)
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(2.dp))
        )

        Column(
            modifier = modifier
                .weight(1f)
        ) {
            Text(
                text = track.trackName,
                maxLines = 1,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Text(
                text = "${track.artistName} • ${track.trackTimeMillis?.toMinutes()}",
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        Icon(
            imageVector = ArrowForwardImg,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

val track1 = Track(
    trackId = 1440651297,
    trackName = "Radio Ga Ga",
    artistName = "Queen",
    trackTimeMillis = 343293,
    artworkUrl100 = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/4d/08/2a/4d082a9e-7898-1aa1-a02f-339810058d9e/14DMGIM05632.rgb.jpg/100x100bb.jpg",
    collectionName = "Greatest Hits I, II & III: The Platinum Collection",
    releaseDate = "1984-01-23T12:00:00Z",
    primaryGenreName = "Rock", country = "USA",
    previewUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview112/v4/94/bc/82/94bc8292-e435-0bdb-634f-6df5c7e02b21/mzaf_7702546962775682802.plus.aac.p.m4a",
    isFavorite = false
)


@Preview(
    name = "Light Theme",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO
)
@Composable
fun PreviewTrackItemLight() {
    AppTheme(darkTheme = false) {
        TrackItem(track = track1)
    }
}

@Preview(
    name = "Dark Theme",
    showBackground = true,
    backgroundColor = 0x000000,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun PreviewTrackItemDark() {
    AppTheme(darkTheme = true) {
        TrackItem(track = track1)
    }
}
