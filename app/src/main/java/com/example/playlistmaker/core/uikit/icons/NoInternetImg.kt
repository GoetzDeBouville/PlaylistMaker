package com.example.playlistmaker.core.uikit.icons

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import kotlin.Suppress

val NoInternetImg: ImageVector
    @Composable
    get() {
        if (_NoInternetImg != null) {
            return _NoInternetImg!!
        }
        _NoInternetImg = ImageVector.Builder(
            name = "NoInternetImg",
            defaultWidth = 120.dp,
            defaultHeight = 120.dp,
            viewportWidth = 120f,
            viewportHeight = 120f
        ).apply {
            path(
                fill = SolidColor(MaterialTheme.colorScheme.secondary),
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
            path(fill = SolidColor(Color(0xFF3772E7))) {
                moveTo(71.5f, 62.5f)
                moveToRelative(-34.5f, 0f)
                arcToRelative(34.5f, 34.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, 69f, 0f)
                arcToRelative(34.5f, 34.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, -69f, 0f)
            }
            path(fill = SolidColor(Color(0xFFFFFFFF))) {
                moveTo(72.16f, 66.53f)
                lineTo(86.31f, 80.69f)
                curveTo(86.67f, 81.04f, 87.15f, 81.23f, 87.65f, 81.23f)
                curveTo(88.15f, 81.22f, 88.63f, 81.02f, 88.98f, 80.67f)
                curveTo(89.33f, 80.32f, 89.53f, 79.84f, 89.54f, 79.34f)
                curveTo(89.54f, 78.84f, 89.35f, 78.36f, 89f, 78f)
                lineTo(54.73f, 43.7f)
                curveTo(54.56f, 43.51f, 54.35f, 43.37f, 54.11f, 43.27f)
                curveTo(53.88f, 43.17f, 53.63f, 43.12f, 53.38f, 43.11f)
                curveTo(53.13f, 43.11f, 52.87f, 43.16f, 52.64f, 43.26f)
                curveTo(52.41f, 43.35f, 52.19f, 43.49f, 52.01f, 43.67f)
                curveTo(51.84f, 43.85f, 51.69f, 44.06f, 51.6f, 44.3f)
                curveTo(51.5f, 44.53f, 51.46f, 44.78f, 51.46f, 45.04f)
                curveTo(51.46f, 45.29f, 51.51f, 45.54f, 51.61f, 45.77f)
                curveTo(51.71f, 46f, 51.86f, 46.21f, 52.04f, 46.39f)
                lineTo(57.94f, 52.29f)
                curveTo(56.71f, 53.11f, 55.55f, 54.06f, 54.47f, 55.14f)
                curveTo(53.49f, 56.13f, 52.61f, 57.2f, 51.82f, 58.35f)
                curveTo(51.68f, 58.55f, 51.58f, 58.79f, 51.53f, 59.03f)
                curveTo(51.47f, 59.28f, 51.47f, 59.53f, 51.51f, 59.77f)
                curveTo(51.6f, 60.27f, 51.89f, 60.71f, 52.3f, 61f)
                curveTo(52.51f, 61.14f, 52.74f, 61.25f, 52.98f, 61.3f)
                curveTo(53.23f, 61.35f, 53.48f, 61.36f, 53.73f, 61.31f)
                curveTo(54.22f, 61.22f, 54.66f, 60.94f, 54.95f, 60.52f)
                curveTo(55.64f, 59.52f, 56.4f, 58.6f, 57.16f, 57.83f)
                curveTo(58.22f, 56.77f, 59.41f, 55.84f, 60.69f, 55.05f)
                lineTo(62.89f, 57.25f)
                curveTo(60.27f, 58.75f, 58.15f, 60.99f, 56.79f, 63.69f)
                curveTo(56.57f, 64.13f, 56.54f, 64.65f, 56.7f, 65.12f)
                curveTo(56.86f, 65.59f, 57.2f, 65.98f, 57.65f, 66.21f)
                curveTo(58.09f, 66.43f, 58.6f, 66.48f, 59.08f, 66.33f)
                curveTo(59.55f, 66.18f, 59.95f, 65.85f, 60.18f, 65.41f)
                curveTo(60.74f, 64.3f, 61.47f, 63.3f, 62.34f, 62.43f)
                curveTo(63.32f, 61.45f, 64.46f, 60.65f, 65.71f, 60.08f)
                lineTo(68.39f, 62.76f)
                curveTo(66.56f, 63.17f, 64.87f, 64.1f, 63.55f, 65.43f)
                curveTo(62.67f, 66.32f, 61.96f, 67.36f, 61.47f, 68.51f)
                curveTo(61.27f, 68.97f, 61.27f, 69.49f, 61.45f, 69.95f)
                curveTo(61.64f, 70.42f, 62f, 70.79f, 62.46f, 71f)
                curveTo(62.92f, 71.2f, 63.44f, 71.21f, 63.91f, 71.03f)
                curveTo(64.37f, 70.85f, 64.75f, 70.49f, 64.96f, 70.03f)
                curveTo(65.28f, 69.31f, 65.71f, 68.66f, 66.24f, 68.13f)
                curveTo(67f, 67.37f, 67.94f, 66.82f, 68.98f, 66.54f)
                curveTo(70.02f, 66.26f, 71.12f, 66.25f, 72.16f, 66.53f)
                close()
                moveTo(69.62f, 55.23f)
                lineTo(73.86f, 59.47f)
                curveTo(75.75f, 60.02f, 77.46f, 61.04f, 78.84f, 62.43f)
                curveTo(79.72f, 63.31f, 80.45f, 64.33f, 81.01f, 65.44f)
                curveTo(81.12f, 65.67f, 81.27f, 65.87f, 81.46f, 66.04f)
                curveTo(81.66f, 66.21f, 81.88f, 66.34f, 82.12f, 66.42f)
                curveTo(82.36f, 66.5f, 82.61f, 66.53f, 82.86f, 66.51f)
                curveTo(83.12f, 66.49f, 83.36f, 66.42f, 83.59f, 66.31f)
                curveTo(83.81f, 66.19f, 84.01f, 66.03f, 84.18f, 65.84f)
                curveTo(84.34f, 65.64f, 84.46f, 65.42f, 84.54f, 65.18f)
                curveTo(84.61f, 64.93f, 84.64f, 64.68f, 84.62f, 64.43f)
                curveTo(84.59f, 64.17f, 84.52f, 63.93f, 84.4f, 63.71f)
                curveTo(83.65f, 62.24f, 82.69f, 60.9f, 81.54f, 59.73f)
                curveTo(79.98f, 58.18f, 78.12f, 56.97f, 76.06f, 56.19f)
                curveTo(74.01f, 55.41f, 71.81f, 55.08f, 69.62f, 55.23f)
                close()
                moveTo(63.88f, 49.48f)
                lineTo(67.02f, 52.62f)
                curveTo(70.62f, 51.94f, 74.35f, 52.33f, 77.73f, 53.73f)
                curveTo(81.12f, 55.14f, 84.03f, 57.5f, 86.09f, 60.53f)
                curveTo(86.23f, 60.74f, 86.42f, 60.91f, 86.63f, 61.05f)
                curveTo(86.84f, 61.19f, 87.07f, 61.28f, 87.32f, 61.33f)
                curveTo(87.56f, 61.38f, 87.82f, 61.37f, 88.06f, 61.32f)
                curveTo(88.31f, 61.27f, 88.54f, 61.17f, 88.75f, 61.03f)
                curveTo(88.96f, 60.88f, 89.13f, 60.7f, 89.27f, 60.49f)
                curveTo(89.4f, 60.28f, 89.5f, 60.04f, 89.54f, 59.79f)
                curveTo(89.59f, 59.55f, 89.58f, 59.29f, 89.53f, 59.05f)
                curveTo(89.47f, 58.8f, 89.37f, 58.57f, 89.23f, 58.36f)
                curveTo(86.5f, 54.37f, 82.57f, 51.36f, 78.01f, 49.76f)
                curveTo(73.45f, 48.16f, 68.5f, 48.06f, 63.88f, 49.48f)
                close()
                moveTo(72.54f, 70.66f)
                curveTo(72.81f, 70.92f, 73.02f, 71.24f, 73.16f, 71.59f)
                curveTo(73.31f, 71.93f, 73.38f, 72.31f, 73.38f, 72.68f)
                curveTo(73.38f, 73.06f, 73.31f, 73.43f, 73.16f, 73.78f)
                curveTo(73.02f, 74.12f, 72.81f, 74.44f, 72.54f, 74.7f)
                curveTo(72.28f, 74.97f, 71.96f, 75.18f, 71.62f, 75.32f)
                curveTo(71.27f, 75.47f, 70.9f, 75.54f, 70.52f, 75.54f)
                curveTo(70.15f, 75.54f, 69.78f, 75.47f, 69.43f, 75.32f)
                curveTo(69.08f, 75.18f, 68.77f, 74.97f, 68.5f, 74.7f)
                curveTo(67.97f, 74.17f, 67.66f, 73.44f, 67.66f, 72.68f)
                curveTo(67.66f, 71.92f, 67.97f, 71.19f, 68.5f, 70.66f)
                curveTo(69.04f, 70.12f, 69.77f, 69.82f, 70.52f, 69.82f)
                curveTo(71.28f, 69.82f, 72.01f, 70.12f, 72.54f, 70.66f)
                close()
            }
            path(
                fill = SolidColor(MaterialTheme.colorScheme.onPrimary),
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

        return _NoInternetImg!!
    }

@Suppress("ObjectPropertyName")
private var _NoInternetImg: ImageVector? = null
