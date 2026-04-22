package com.example.ej26cardexroom.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Blue80,
    secondary = BlueGrey80,
    tertiary = Sky80
)

private val LightColorScheme = lightColorScheme(
    primary = Blue40,
    onPrimary = BlueSurface,
    primaryContainer = Blue80,
    onPrimaryContainer = BlueText,
    secondary = BlueGrey40,
    onSecondary = BlueSurface,
    secondaryContainer = Sky80,
    onSecondaryContainer = BlueText,
    tertiary = Sky40,
    background = BlueBackground,
    onBackground = BlueText,
    surface = BlueSurface,
    onSurface = BlueText,
    surfaceVariant = BlueSurfaceSoft,
    onSurfaceVariant = BlueGrey40,
    outline = BlueOutline
)

@Composable
fun Ej26CardexRoomTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
