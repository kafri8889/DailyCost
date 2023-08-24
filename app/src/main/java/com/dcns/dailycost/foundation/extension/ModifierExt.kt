package com.dcns.dailycost.foundation.extension

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.dailyCostMarquee(
	leftEdge: Boolean = false,
	edgeWidth: Dp = 8.dp,
	delayMillis: Int = 2000
): Modifier {
	return this
		// Rendering to an offscreen buffer is required to get the faded edges' alpha to be
		// applied only to the text, and not whatever is drawn below this composable (e.g. the
		// window).
		.graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
		.drawWithContent {
			drawContent()
			drawFadedEdge(
				edgeWidth = edgeWidth,
				leftEdge = leftEdge
			)
		}
		.basicMarquee(
			delayMillis = delayMillis
		)
}
