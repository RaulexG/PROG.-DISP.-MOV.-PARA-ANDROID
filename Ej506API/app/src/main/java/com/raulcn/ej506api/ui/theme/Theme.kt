package com.raulcn.ej506api.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = OceanBlue,
    onPrimary = CardWhite,
    primaryContainer = SoftMint,
    onPrimaryContainer = DeepInk,
    secondary = Aqua,
    background = Mist,
    onBackground = DeepInk,
    surface = CardWhite,
    onSurface = DeepInk,
    surfaceVariant = SoftMint,
    onSurfaceVariant = Slate
)

@Composable
fun Ej506APITheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
