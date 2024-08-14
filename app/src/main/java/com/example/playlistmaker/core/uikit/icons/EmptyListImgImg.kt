package com.example.playlistmaker.core.uikit.icons

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.theme.AppTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

val EmptyListImg: ImageVector
    @Composable
    get() {
        if (_EmptyListImg != null) {
            return _EmptyListImg!!
        }
        val onBackground = MaterialTheme.colorScheme.onBackground
        _EmptyListImg = remember(onBackground) {
            ImageVector.Builder(
                name = "EmptyListImg",
                defaultWidth = 120.dp,
                defaultHeight = 120.dp,
                viewportWidth = 120f,
                viewportHeight = 120f
            ).apply {
                path(
                    fill = SolidColor(onBackground),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(34f, 29.5f)
                    lineTo(111f, 3f)
                    verticalLineTo(31.5f)
                    lineTo(40f, 42.1f)
                    verticalLineTo(76.5f)
                    verticalLineTo(79f)
                    horizontalLineTo(39.89f)
                    curveTo(38.95f, 89.14f, 32.42f, 97f, 24.5f, 97f)
                    curveTo(15.94f, 97f, 9f, 87.82f, 9f, 76.5f)
                    curveTo(9f, 65.18f, 15.94f, 56f, 24.5f, 56f)
                    curveTo(28.08f, 56f, 31.38f, 57.6f, 34f, 60.3f)
                    verticalLineTo(43f)
                    verticalLineTo(35f)
                    verticalLineTo(29.5f)
                    close()
                }
                path(fill = SolidColor(Color(0xFF1A1B22))) {
                    moveTo(71.5f, 62.5f)
                    moveToRelative(-34.5f, 0f)
                    arcToRelative(34.5f, 34.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, 69f, 0f)
                    arcToRelative(34.5f, 34.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, -69f, 0f)
                }
                path(fill = SolidColor(Color(0xFFFCD452))) {
                    moveTo(71.5f, 28f)
                    curveTo(62.35f, 28f, 53.57f, 31.63f, 47.1f, 38.1f)
                    curveTo(40.63f, 44.57f, 37f, 53.35f, 37f, 62.5f)
                    curveTo(37f, 71.65f, 40.63f, 80.43f, 47.1f, 86.9f)
                    curveTo(53.57f, 93.37f, 62.35f, 97f, 71.5f, 97f)
                    curveTo(80.65f, 97f, 89.43f, 93.37f, 95.9f, 86.9f)
                    curveTo(102.36f, 80.43f, 106f, 71.65f, 106f, 62.5f)
                    curveTo(106f, 53.35f, 102.36f, 44.57f, 95.9f, 38.1f)
                    curveTo(89.43f, 31.63f, 80.65f, 28f, 71.5f, 28f)
                    close()
                    moveTo(65.03f, 56.03f)
                    curveTo(65.03f, 57.17f, 64.58f, 58.27f, 63.77f, 59.08f)
                    curveTo(62.96f, 59.89f, 61.86f, 60.34f, 60.72f, 60.34f)
                    curveTo(59.58f, 60.34f, 58.48f, 59.89f, 57.67f, 59.08f)
                    curveTo(56.86f, 58.27f, 56.41f, 57.17f, 56.41f, 56.03f)
                    curveTo(56.41f, 54.89f, 56.86f, 53.79f, 57.67f, 52.98f)
                    curveTo(58.48f, 52.17f, 59.58f, 51.72f, 60.72f, 51.72f)
                    curveTo(61.86f, 51.72f, 62.96f, 52.17f, 63.77f, 52.98f)
                    curveTo(64.58f, 53.79f, 65.03f, 54.89f, 65.03f, 56.03f)
                    close()
                    moveTo(82.28f, 51.72f)
                    curveTo(83.43f, 51.72f, 84.52f, 52.17f, 85.33f, 52.98f)
                    curveTo(86.14f, 53.79f, 86.59f, 54.89f, 86.59f, 56.03f)
                    curveTo(86.59f, 57.17f, 86.14f, 58.27f, 85.33f, 59.08f)
                    curveTo(84.52f, 59.89f, 83.43f, 60.34f, 82.28f, 60.34f)
                    curveTo(81.14f, 60.34f, 80.04f, 59.89f, 79.23f, 59.08f)
                    curveTo(78.42f, 58.27f, 77.97f, 57.17f, 77.97f, 56.03f)
                    curveTo(77.97f, 54.89f, 78.42f, 53.79f, 79.23f, 52.98f)
                    curveTo(80.04f, 52.17f, 81.14f, 51.72f, 82.28f, 51.72f)
                    close()
                    moveTo(83.59f, 71.13f)
                    horizontalLineTo(87.03f)
                    curveTo(87.6f, 71.13f, 88.15f, 71.35f, 88.55f, 71.76f)
                    curveTo(88.95f, 72.16f, 89.18f, 72.71f, 89.18f, 73.28f)
                    curveTo(89.18f, 73.85f, 88.95f, 74.4f, 88.55f, 74.81f)
                    curveTo(88.15f, 75.21f, 87.6f, 75.44f, 87.03f, 75.44f)
                    horizontalLineTo(83.59f)
                    curveTo(78.94f, 75.44f, 74.4f, 76.81f, 70.54f, 79.39f)
                    curveTo(70.3f, 79.55f, 70.04f, 79.67f, 69.75f, 79.73f)
                    curveTo(69.47f, 79.8f, 69.18f, 79.8f, 68.9f, 79.75f)
                    curveTo(68.61f, 79.7f, 68.34f, 79.59f, 68.1f, 79.43f)
                    curveTo(67.86f, 79.27f, 67.66f, 79.06f, 67.5f, 78.82f)
                    curveTo(67.34f, 78.58f, 67.23f, 78.31f, 67.17f, 78.03f)
                    curveTo(67.12f, 77.75f, 67.12f, 77.46f, 67.18f, 77.17f)
                    curveTo(67.24f, 76.89f, 67.36f, 76.62f, 67.53f, 76.39f)
                    curveTo(67.69f, 76.15f, 67.9f, 75.95f, 68.15f, 75.8f)
                    curveTo(72.72f, 72.75f, 78.09f, 71.13f, 83.59f, 71.13f)
                    close()
                }
                path(
                    fill = SolidColor(onBackground),
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(111f, 6f)
                    lineTo(105f, 18.21f)
                    verticalLineTo(79.3f)
                    curveTo(102.38f, 76.61f, 99.08f, 75f, 95.5f, 75f)
                    curveTo(86.94f, 75f, 80f, 84.18f, 80f, 95.5f)
                    curveTo(80f, 106.82f, 86.94f, 116f, 95.5f, 116f)
                    curveTo(103.68f, 116f, 110.38f, 107.62f, 110.96f, 97f)
                    horizontalLineTo(111f)
                    verticalLineTo(95.5f)
                    verticalLineTo(6f)
                    close()
                }
            }.build()
        }

        return _EmptyListImg!!
    }

@Suppress("ObjectPropertyName")
private var _EmptyListImg: ImageVector? = null





@Preview(
    name = "Light Theme",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO
)
@Composable
fun PreviewEmptyMessageLight() {
    AppTheme(darkTheme = false) {
        Image(
            imageVector = EmptyListImg,
            contentDescription = null,
        )
    }
}

@Preview(
    name = "Dark Theme",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun PreviewEmptyListImgDark() {
    AppTheme(darkTheme = true) {
        Image(
            imageVector = EmptyListImg,
            contentDescription = null,
        )
    }
}
