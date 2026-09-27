package com.cognitech.mindflow.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = CornflowerBlue,
    onPrimary = White,
    secondary = Downy,
    onSecondary = White,
    background = White,
    onBackground = MineShaft,
    surface = White,
    onSurface = MineShaft,
    onSurfaceVariant = Gray,
    outline = Mercury,
    error = SunsetOrange,
)

@Composable
fun MindFlowTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = LightColors, typography = MindFlowTypography, content = content)
}
