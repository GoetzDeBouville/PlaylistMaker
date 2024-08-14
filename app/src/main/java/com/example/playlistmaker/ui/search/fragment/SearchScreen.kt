package com.example.playlistmaker.ui.search.fragment

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.core.uikit.EmptyMessage
import com.example.playlistmaker.core.uikit.ErrorConnectionMessage
import com.example.playlistmaker.core.uikit.SearchTextField
import com.example.playlistmaker.theme.AppTheme

@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = context.getString(R.string.search),
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = modifier
                .padding(vertical = 16.dp)
        )

        SearchTextField(
            text = "",
            hint = context.getString(R.string.search),
            onTextChange = { inputText ->
                text = inputText
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            EmptyMessage(
                messageText = context.getString(R.string.nothing_found)
            )

//            ErrorConnectionMessage(
//                messageText = "Нет интернета",
//                buttonText = "обновить"
//            ) {
//
//            }
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
fun PreviewSettingsScreenLight() {
    AppTheme(darkTheme = false) {
        SearchScreen(Modifier)
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
fun PreviewSettingsScreenDark() {
    AppTheme(darkTheme = true) {
        SearchScreen(Modifier)
    }
}
