package com.example.playlistmaker.core.uikit

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.playlistmaker.core.uikit.icons.EmptyListImg
import com.example.playlistmaker.theme.AppTheme

@Composable
fun EmptyMessage(
    modifier: Modifier = Modifier,
    messageText: String
) {
    Column {
        Image(
            modifier = modifier.align(Alignment.CenterHorizontally),
            imageVector = EmptyListImg,
            contentDescription = null,
        )
        Text(
            modifier = modifier.align(Alignment.CenterHorizontally),
            text = messageText,
            style = MaterialTheme.typography.displayMedium.copy(
                MaterialTheme.colorScheme.secondary
            )
        )
    }
}


@Preview(
    name = "Light Theme",
    showBackground = true,
    device = Devices.PIXEL_4,
    widthDp = 360,
    heightDp = 640,
    uiMode = UI_MODE_NIGHT_NO
)
@Composable
fun PreviewEmptyMessageLight() {
    AppTheme(darkTheme = false) {
        EmptyMessage(messageText = "Пустой список")
    }
}

@Preview(
    name = "Dark Theme",
    showBackground = true,
    device = Devices.PIXEL_4,
    widthDp = 360,
    heightDp = 640,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun PreviewTEmptyMessageDark() {
    AppTheme(darkTheme = true) {
        EmptyMessage(messageText = "Пустой список")
    }
}
