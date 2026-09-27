package com.cognitech.mindflow.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Tokens del Figma "Web Application" (página 2 - mockups móviles)
val MineShaft = Color(0xFF2F2F2F)       // color/grey/18 - texto principal
val Gray = Color(0xFF828282)            // color/grey/51 - texto secundario
val Boulder = Color(0xFF757575)         // color/grey/46 - placeholders
val DoveGray = Color(0xFF666666)        // color/grey/40
val Silver = Color(0xFFCCCCCC)          // color/grey/80
val Mercury = Color(0xFFE5E5E5)         // color/grey/90 - bordes
val Gallery = Color(0xFFEFEFEF)         // color/grey/94
val CatskillWhite = Color(0xFFF5F7FA)   // color/grey/97 - fondos de campos
val MenuButtonBg = Color(0xFFF7F7FC)
val White = Color(0xFFFFFFFF)

val CornflowerBlue = Color(0xFF4F8DF5)  // color/azure/64
val Downy = Color(0xFF6ED3A3)           // color/spring green/63
val GoldenTainoi = Color(0xFFFFD166)    // color/orange/70
val VividTangerine = Color(0xFFFF8A8A)  // color/red/77
val Zest = Color(0xFFE28F22)            // color/orange/51
val Serenade = Color(0xFFFFF4E5)        // color/grey/95
val Portage = Color(0xFF8A7CF6)         // color/blue/73
val SunsetOrange = Color(0xFFFF4A4A)    // color/red/65

// Colores de la pantalla de Analíticas
val Gray800 = Color(0xFF1F2937)
val Gray500 = Color(0xFF6B7280)
val Gray400 = Color(0xFF9CA3AF)
val Gray50 = Color(0xFFF9FAFB)
val Gray100 = Color(0xFFF3F4F6)
val Purple500 = Color(0xFFA855F7)
val Blue400 = Color(0xFF60A5FA)
val Orange400 = Color(0xFFFB923C)

val MindGradient = Brush.linearGradient(listOf(CornflowerBlue, Downy))
val MindGradientSoft = Brush.linearGradient(
    listOf(CornflowerBlue.copy(alpha = 0.05f), Downy.copy(alpha = 0.05f))
)
