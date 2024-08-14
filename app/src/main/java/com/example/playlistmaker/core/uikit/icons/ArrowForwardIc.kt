package com.example.playlistmaker.core.uikit.icons

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ArrowForwardImg: ImageVector
    @Composable
    get() {
        if (_ArrowForward != null) {
            return _ArrowForward!!
        }
        _ArrowForward = ImageVector.Builder(
            name = "ArrowForward",
            defaultWidth = 8.dp,
            defaultHeight = 14.dp,
            viewportWidth = 8f,
            viewportHeight = 14f
        ).apply {
            path(fill = SolidColor(MaterialTheme.colorScheme.secondary)) {
                moveTo(0f, 12.748f)
                lineTo(1.213f, 14f)
                lineTo(8f, 7f)
                lineTo(1.213f, 0f)
                lineTo(0f, 1.252f)
                lineTo(5.573f, 7f)
                lineTo(0f, 12.748f)
                close()
            }
        }.build()

        return _ArrowForward!!
    }

@Suppress("ObjectPropertyName")
private var _ArrowForward: ImageVector? = null
