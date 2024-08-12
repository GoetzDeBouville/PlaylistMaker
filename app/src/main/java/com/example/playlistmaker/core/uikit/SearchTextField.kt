package com.example.playlistmaker.core.uikit

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.theme.AppTheme
import com.example.playlistmaker.theme.BlueText


@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    text: String = "",
    hint: String = "",
    onTextChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Box {
        BasicTextField(
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            value = text,
            onValueChange = onTextChange,
            singleLine = true,
            cursorBrush = SolidColor(BlueText),
            decorationBox = { innerTextField ->
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (leadingIcon != null) {
                        leadingIcon()
                    }
                    Box(
                        modifier = modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                modifier = Modifier.padding(start = 8.dp),
                                text = hint,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                            )
                        }
                        innerTextField()
                    }
                    if (text.isNotEmpty()) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_clear),
                            contentDescription = "Clear Icon",
                            tint = MaterialTheme.colorScheme.surfaceContainer,
                            modifier = Modifier.clickable { onTextChange("") }
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search)
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
    var text by remember { mutableStateOf("") }
    AppTheme(darkTheme = false) {
        SearchTextField(
            text = "",
            hint = LocalContext.current.getString(R.string.search),
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
    }
}

@Preview(
    name = "Dark Theme",
    showBackground = true,
    backgroundColor = 0x000000,
    device = Devices.PIXEL_4,
    widthDp = 360,
    heightDp = 640,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun PreviewTextFieldDark() {
    var text by remember { mutableStateOf("") }
    AppTheme(darkTheme = true) {
        SearchTextField(
            text = "",
            hint = LocalContext.current.getString(R.string.search),
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
    }
}
