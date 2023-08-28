package com.dcns.dailycost.foundation.uicomponent.bubble_bar

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

object BubbleBarTokens {

	val ContainerColor: Color @Composable get() = MaterialTheme.colorScheme.inverseSurface
	val IconColor: Color @Composable get() = MaterialTheme.colorScheme.inverseOnSurface
	val SupportingTextColor: Color @Composable get() = MaterialTheme.colorScheme.inverseOnSurface
	val ActionLabelTextColor: Color @Composable get() = MaterialTheme.colorScheme.inversePrimary
	val ActionLabelTextFont: TextStyle @Composable get() = MaterialTheme.typography.labelLarge
	val SupportingTextFont: TextStyle @Composable get() = MaterialTheme.typography.bodyMedium
	val ContainerShape: Shape @Composable get() = MaterialTheme.shapes.extraSmall
	val ContainerElevation = 6.0.dp
	val IconSize = 24.0.dp

}
