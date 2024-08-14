package com.example.playlistmaker.core.uikit.icons

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val EmptyPosterImg: ImageVector
    @Composable
    get() {
        if (_EmptyPoster != null) {
            return _EmptyPoster!!
        }
        _EmptyPoster = ImageVector.Builder(
            name = "EmptyPoster",
            defaultWidth = 35.dp,
            defaultHeight = 35.dp,
            viewportWidth = 35f,
            viewportHeight = 35f
        ).apply {
            path(fill = SolidColor(MaterialTheme.colorScheme.secondary)) {
                moveTo(3.606f, 7.919f)
                curveTo(4.919f, 6.588f, 6.231f, 5.387f, 6.812f, 5.631f)
                curveTo(7.75f, 6.006f, 6.812f, 7.563f, 6.25f, 8.481f)
                curveTo(5.781f, 9.269f, 0.887f, 15.775f, 0.887f, 20.313f)
                curveTo(0.887f, 22.712f, 1.787f, 24.7f, 3.4f, 25.9f)
                curveTo(4.806f, 26.95f, 6.662f, 27.269f, 8.35f, 26.763f)
                curveTo(10.356f, 26.181f, 12.006f, 24.138f, 14.087f, 21.569f)
                curveTo(16.356f, 18.775f, 19.394f, 15.119f, 21.737f, 15.119f)
                curveTo(24.794f, 15.119f, 24.831f, 17.013f, 25.038f, 18.475f)
                curveTo(17.95f, 19.675f, 14.95f, 25.356f, 14.95f, 28.544f)
                curveTo(14.95f, 31.731f, 17.65f, 34.338f, 20.969f, 34.338f)
                curveTo(24.025f, 34.338f, 29.013f, 31.844f, 29.763f, 22.9f)
                horizontalLineTo(34.375f)
                verticalLineTo(18.212f)
                horizontalLineTo(29.744f)
                curveTo(29.462f, 15.119f, 27.7f, 10.337f, 22.188f, 10.337f)
                curveTo(17.969f, 10.337f, 14.35f, 13.919f, 12.925f, 15.663f)
                curveTo(11.837f, 17.031f, 9.062f, 20.313f, 8.631f, 20.763f)
                curveTo(8.162f, 21.325f, 7.356f, 22.337f, 6.55f, 22.337f)
                curveTo(5.706f, 22.337f, 5.2f, 20.781f, 5.875f, 18.737f)
                curveTo(6.531f, 16.694f, 8.5f, 13.375f, 9.344f, 12.137f)
                curveTo(10.806f, 10f, 11.781f, 8.538f, 11.781f, 5.988f)
                curveTo(11.781f, 1.919f, 8.706f, 0.625f, 7.075f, 0.625f)
                curveTo(4.6f, 0.625f, 2.444f, 2.5f, 1.975f, 2.969f)
                curveTo(1.3f, 3.644f, 0.737f, 4.206f, 0.325f, 4.713f)
                lineTo(3.606f, 7.919f)
                close()
                moveTo(21.025f, 29.781f)
                curveTo(20.444f, 29.781f, 19.638f, 29.294f, 19.638f, 28.431f)
                curveTo(19.638f, 27.306f, 21.006f, 24.306f, 25.019f, 23.256f)
                curveTo(24.456f, 28.3f, 22.337f, 29.781f, 21.025f, 29.781f)
                close()
            }
        }.build()

        return _EmptyPoster!!
    }

@Suppress("ObjectPropertyName")
private var _EmptyPoster: ImageVector? = null
