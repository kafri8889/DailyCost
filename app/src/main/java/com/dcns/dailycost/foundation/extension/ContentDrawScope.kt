package com.dcns.dailycost.foundation.extension

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.unit.Dp

fun ContentDrawScope.drawFadedEdge(
    edgeWidth: Dp,
    leftEdge: Boolean
) {
    val edgeWidthPx = edgeWidth.toPx()
    drawRect(
        size = Size(edgeWidthPx, size.height),
        topLeft = Offset(if (leftEdge) 0f else size.width - edgeWidthPx, 0f),
        blendMode = BlendMode.DstIn,
        brush = Brush.horizontalGradient(
            colors = listOf(Color.Transparent, Color.Black),
            startX = if (leftEdge) 0f else size.width,
            endX = if (leftEdge) edgeWidthPx else size.width - edgeWidthPx
        )
    )
}
