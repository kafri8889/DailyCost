package com.dcns.dailycost.foundation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class DailyCostColorScheme(
	val primary: Color,
	val primaryContainer: Color,
	val onPrimary: Color,
	val labelText: Color,
	val text: Color,
	val outline: Color,
	val expense: Color,
	val income: Color,
	val wildSand: Color,
	val silverChalice: Color,
)

val LocalDailyCostColorScheme = staticCompositionLocalOf {
	DailyCostColorScheme(
		primary = Color.Unspecified,
		primaryContainer = Color.Unspecified,
		onPrimary = Color.Unspecified,
		labelText = Color.Unspecified,
		text = Color.Unspecified,
		outline = Color.Unspecified,
		expense = Color.Unspecified,
		income = Color.Unspecified,
		wildSand = Color.Unspecified,
		silverChalice = Color.Unspecified,
	)
}

object DailyCostTheme {
	val colorScheme: DailyCostColorScheme
		@Composable
		get() = LocalDailyCostColorScheme.current
}
