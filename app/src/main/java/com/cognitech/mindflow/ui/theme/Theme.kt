package com.cognitech.mindflow.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = MindBlue,
    onPrimary = Color.White,
    secondary = MindGreen,
    onSecondary = Color.White,
    background = Color.White,
    onBackground = TextPrimary,
    surface = Color.White,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = FieldBorder,
    error = ErrorRed,
)

@Composable
fun MindFlowTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = LightColors, content = content)
}
