package com.opticeasy.app.ui.theme
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme

import androidx.compose.ui.platform.LocalContext
import com.opticeasy.app.ui.theme.OpticBackground
import com.opticeasy.app.ui.theme.OpticOnBackground
import com.opticeasy.app.ui.theme.OpticOnPrimary
import com.opticeasy.app.ui.theme.OpticOutline
import com.opticeasy.app.ui.theme.OpticPrimary
import com.opticeasy.app.ui.theme.OpticSecondary


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = OpticPrimary,
    onPrimary = OpticOnPrimary,

    primaryContainer = OpticSecondary,
    onPrimaryContainer = OpticOnBackground,

    secondary = OpticSecondary,
    onSecondary = OpticOnPrimary,

    secondaryContainer = OpticCardBg,
    onSecondaryContainer = OpticOnBackground,

    background = OpticBackground,
    onBackground = OpticOnBackground,

    surface = OpticSurface,
    onSurface = OpticOnBackground,

    surfaceVariant = OpticCardBg,
    onSurfaceVariant = OpticOnBackground,

    outline = OpticOutline,
    outlineVariant = OpticOutline,

    inverseSurface = OpticOnBackground,
    inverseOnSurface = OpticSurface,

    scrim = Color(0x66000000)
)

@Composable
fun OpticEasyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}