package com.cognitech.mindflow.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import com.cognitech.mindflow.R

val Inter = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold),
)

private val Base = Typography()

val MindFlowTypography = Typography(
    displayLarge = Base.displayLarge.copy(fontFamily = Inter, lineHeight = TextUnit.Unspecified),
    displayMedium = Base.displayMedium.copy(fontFamily = Inter, lineHeight = TextUnit.Unspecified),
    displaySmall = Base.displaySmall.copy(fontFamily = Inter, lineHeight = TextUnit.Unspecified),
    headlineLarge = Base.headlineLarge.copy(fontFamily = Inter, lineHeight = TextUnit.Unspecified),
    headlineMedium = Base.headlineMedium.copy(fontFamily = Inter, lineHeight = TextUnit.Unspecified),
    headlineSmall = Base.headlineSmall.copy(fontFamily = Inter, lineHeight = TextUnit.Unspecified),
    titleLarge = Base.titleLarge.copy(fontFamily = Inter, lineHeight = TextUnit.Unspecified),
    titleMedium = Base.titleMedium.copy(fontFamily = Inter, lineHeight = TextUnit.Unspecified),
    titleSmall = Base.titleSmall.copy(fontFamily = Inter, lineHeight = TextUnit.Unspecified),
    bodyLarge = Base.bodyLarge.copy(fontFamily = Inter, lineHeight = TextUnit.Unspecified),
    bodyMedium = Base.bodyMedium.copy(fontFamily = Inter, lineHeight = TextUnit.Unspecified),
    bodySmall = Base.bodySmall.copy(fontFamily = Inter, lineHeight = TextUnit.Unspecified),
    labelLarge = Base.labelLarge.copy(fontFamily = Inter, lineHeight = TextUnit.Unspecified),
    labelMedium = Base.labelMedium.copy(fontFamily = Inter, lineHeight = TextUnit.Unspecified),
    labelSmall = Base.labelSmall.copy(fontFamily = Inter, lineHeight = TextUnit.Unspecified),
)
