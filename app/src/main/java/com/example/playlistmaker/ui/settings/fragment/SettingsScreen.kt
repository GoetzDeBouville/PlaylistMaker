package com.example.playlistmaker.ui.settings.fragment

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.theme.AppTheme
import com.example.playlistmaker.theme.BlueLight
import com.example.playlistmaker.theme.BlueText
import com.example.playlistmaker.theme.LightGray
import com.example.playlistmaker.theme.TextGray
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel


@Composable
fun SettingsScreen(modifier: Modifier, viewModel: SettingsViewModel = viewModel()) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = context.getString(R.string.settings),
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = modifier
                .padding(vertical = 16.dp)
                .padding(bottom = 40.dp)
        )

        Row(
            modifier = modifier
                .clickable {
                    viewModel.updateThemeSettings(!uiState.themeIsDark)
                }
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = context.getString(R.string.dark_theme),
                style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onBackground),
                modifier = modifier
                    .padding(vertical = 20.dp)
                    .weight(1f)
            )

            Switch(
                checked = uiState.themeIsDark,
                onCheckedChange = {
                    viewModel.updateThemeSettings(!uiState.themeIsDark)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = BlueText,
                    checkedTrackColor = BlueLight,
                    uncheckedThumbColor = TextGray,
                    uncheckedTrackColor = LightGray
                )
            )
        }

        SettingsRow(
            modifier = modifier,
            R.string.share_app,
            R.drawable.share
        ) {
            viewModel.shareApp()
        }

        SettingsRow(
            modifier = modifier,
            R.string.text_to_support,
            R.drawable.support
        ) {
            viewModel.openSupport()
        }

        SettingsRow(
            modifier = modifier,
            R.string.user_agreement,
            R.drawable.arrow_forward
        ) {
            viewModel.openTerms()
        }
    }
}

@Composable
fun SettingsRow(
    modifier: Modifier,
    textResource: Int,
    drawableResource: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable { onClick.invoke() }
            .padding(vertical = 20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = LocalContext.current.getString(textResource),
            style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(id = drawableResource), contentDescription = "",
            tint = MaterialTheme.colorScheme.onSurface
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
fun PreviewSettingsScreenLight() {
    AppTheme(darkTheme = false) {
        SettingsScreen(Modifier)
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
        SettingsScreen(Modifier)
    }
}
