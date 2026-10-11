package ru.mazenin.kotlin

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val palette =
    lightColorScheme(
        primary = Color(0xFF175C52),
        onPrimary = Color.White,
        secondary = Color(0xFF8B5B21),
        background = Color(0xFFF4F7F4),
        surface = Color(0xFFF4F7F4),
        primaryContainer = Color(0xFFD3ECE1),
        onPrimaryContainer = Color(0xFF153C32),
    )
private val type =
    Typography(
        headlineMedium =
            TextStyle(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                lineHeight = 34.sp,
            ),
        titleLarge =
            TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                lineHeight = 28.sp,
            ),
        bodyLarge =
            TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 17.sp, lineHeight = 25.sp),
    )

@Composable
fun StudentTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = palette, typography = type, content = content)
}
