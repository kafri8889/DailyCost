package com.dcns.dailycost.foundation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class DailyCostColorScheme(
    val primary: Color,
    val onPrimary: Color,
    val labelText: Color,
    val text: Color,
    val outline: Color,
)

val LocalDailyCostColorScheme = staticCompositionLocalOf {
    DailyCostColorScheme(
        primary = Color.Unspecified,
        onPrimary = Color.Unspecified,
        labelText = Color.Unspecified,
        text = Color.Unspecified,
        outline = Color.Unspecified,
    )
}

object DailyCostTheme {
    val colorScheme: DailyCostColorScheme
        @Composable
        get() = LocalDailyCostColorScheme.current
}
