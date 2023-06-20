package com.dcns.dailycost.foundation.uicomponent

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.material.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun AnimatedText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current
) {

    AnimatedContent(
        label = "animated_text",
        targetState = text,
        transitionSpec = {
            // Compare the incoming number with the previous number.
            if (targetState > initialState) {
                // If the target number is larger, it slides up and fades in
                // while the initial (smaller) number slides up and fades out.
                slideInVertically { height -> height } + fadeIn() togetherWith
                        slideOutVertically { height -> -height } + fadeOut()
            } else {
                // If the target number is smaller, it slides down and fades in
                // while the initial number slides down and fades out.
                slideInVertically { height -> -height } + fadeIn() togetherWith
                        slideOutVertically { height -> height } + fadeOut()
            }.using(
                // Disable clipping since the faded slide-in/out should
                // be displayed out of bounds.
                SizeTransform(clip = false)
            )
        }
    ) { targetCount ->
        Text(
            text = targetCount,
            style = style,
            modifier = modifier
        )
    }
}

@Composable
fun AnimatedTextByChar(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current
) {
    var oldCount by remember { mutableStateOf(text) }

    SideEffect {
        oldCount = text
    }

    Row(modifier = modifier) {
        val oldCountString = oldCount

        for (i in text.indices) {
            val oldChar = oldCountString.getOrNull(i)
            val newChar = text[i]
            val char = if (oldChar == newChar) {
                oldCountString[i]
            } else {
                text[i]
            }

            AnimatedContent(
                label = "animated_text_by_char",
                targetState = char,
                transitionSpec = {
                    // Compare the incoming number with the previous number.
                    if (targetState > initialState) {
                        // If the target number is larger, it slides up and fades in
                        // while the initial (smaller) number slides up and fades out.
                        slideInVertically { height -> height } + fadeIn() togetherWith
                                slideOutVertically { height -> -height } + fadeOut()
                    } else {
                        // If the target number is smaller, it slides down and fades in
                        // while the initial number slides down and fades out.
                        slideInVertically { height -> -height } + fadeIn() togetherWith
                                slideOutVertically { height -> height } + fadeOut()
                    }.using(
                        // Disable clipping since the faded slide-in/out should
                        // be displayed out of bounds.
                        SizeTransform(clip = false)
                    )
                }
            ) { mChar ->
                Text(
                    text = mChar.toString(),
                    style = style,
                    softWrap = false
                )
            }
        }
    }
}
