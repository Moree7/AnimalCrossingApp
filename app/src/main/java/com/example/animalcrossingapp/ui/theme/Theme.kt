package com.example.animalcrossingapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Verde,
    onPrimary = Color.White,

    secondary = Azul,
    onSecondary = Color.White,

    tertiary = Rosa,
    onTertiary = Marron_Oscuro,

    background = Color(0xFF121212),
    onBackground = Color(0xFFF2F2F2),

    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFF2F2F2)
)

private val LightColorScheme = lightColorScheme(
    primary = Azul_Verde,
    onPrimary = Color.White,

    secondary = Amarillo,
    onSecondary = Marron_Oscuro,

    tertiary = Rosa,
    onTertiary = Marron_Oscuro,

    background = Beige,
    onBackground = Marron_Oscuro,

    surface = Color.White,
    onSurface = Marron_Oscuro
)

@Composable
fun AnimalCrossingAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}