package com.dcns.dailycost.foundation.uicomponent

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.IconButton
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.dcns.dailycost.foundation.common.NoRippleTheme

@Composable
fun NoRippleIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalRippleTheme provides NoRippleTheme
    ) {
        IconButton(
            onClick = onClick,
            interactionSource = interactionSource,
            modifier = modifier,
            enabled = enabled
        ) {
            content()
        }
    }
}
