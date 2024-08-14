package com.example.playlistmaker.core.uikit

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.core.uikit.icons.NoInternetImg
import com.example.playlistmaker.theme.AppTheme

@Composable
fun ErrorConnectionMessage(
    modifier: Modifier = Modifier,
    messageText: String,
    buttonText: String,
    onButtonClick: () -> Unit
) {
    Column {
        Image(
            modifier = modifier.align(Alignment.CenterHorizontally),
            imageVector = NoInternetImg,
            contentDescription = null,
        )
        Text(
            modifier = modifier.align(Alignment.CenterHorizontally),
            text = messageText,
            style = MaterialTheme.typography.displayMedium.copy(
                MaterialTheme.colorScheme.secondary
            )
        )
        Button(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 24.dp),
            onClick = onButtonClick,
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.onBackground,
                contentColor = MaterialTheme.colorScheme.background,
                disabledContentColor = Color.Unspecified,
                disabledContainerColor = Color.Unspecified
            )
        ) {
            Text(
                text = buttonText,
                style = MaterialTheme.typography.displayMedium.copy(
                    color = MaterialTheme.colorScheme.surfaceTint
                )
            )
        }
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
fun PreviewErrorConnectionMessageLight() {
    AppTheme(darkTheme = false) {
        ErrorConnectionMessage(
            messageText = "Нет интернета",
            buttonText = "обновить"
        ) {

        }
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
fun PreviewTErrorConnectionMessageDark() {
    AppTheme(darkTheme = false) {
        ErrorConnectionMessage(
            messageText = "Нет интернета",
            buttonText = "обновить"
        ) {

        }
    }
}
