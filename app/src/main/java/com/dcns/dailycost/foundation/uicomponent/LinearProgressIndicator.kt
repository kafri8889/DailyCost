package com.dcns.dailycost.foundation.uicomponent

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dcns.dailycost.foundation.theme.DailyCostTheme

@Preview
@Composable
private fun LinearProgressIndicatorPreview() {
    DailyCostTheme {
        LinearProgressIndicator(
            progress = 0.5f,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun LinearProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    animationSpec: AnimationSpec<Float> = tween(256),
    color: Color = ProgressIndicatorDefaults.linearColor,
    trackColor: Color = ProgressIndicatorDefaults.linearTrackColor
) {

    Box(
        modifier = modifier
            .heightIn(min = 4.dp)
            .clip(CircleShape)
            .background(trackColor)
            .composed {
                val mProgress by animateFloatAsState(
                    label = "progress",
                    targetValue = progress.coerceIn(0f..1f),
                    animationSpec = animationSpec
                )

                drawBehind {
                    drawRoundRect(
                        color = color,
                        size = Size(size.width * mProgress, size.height),
                        cornerRadius = CornerRadius(100f, 100f)
                    )
                }
            }
    )
}
