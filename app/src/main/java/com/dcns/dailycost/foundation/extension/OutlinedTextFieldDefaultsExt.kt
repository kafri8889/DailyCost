package com.dcns.dailycost.foundation.extension

import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.dcns.dailycost.foundation.theme.DailyCostTheme

@Composable
fun OutlinedTextFieldDefaults.dailyCostColor(
    cursorColor: Color = DailyCostTheme.colorScheme.primary,
    focusedLabelColor: Color = DailyCostTheme.colorScheme.primary,
    focusedBorderColor: Color = DailyCostTheme.colorScheme.primary,
    unfocusedBorderColor: Color = DailyCostTheme.colorScheme.outline,
): TextFieldColors {
    return colors(
        cursorColor = cursorColor,
        focusedLabelColor = focusedLabelColor,
        focusedBorderColor = focusedBorderColor,
        unfocusedBorderColor = unfocusedBorderColor,
        selectionColors = TextSelectionColors(
            handleColor = DailyCostTheme.colorScheme.primary,
            backgroundColor = LocalTextSelectionColors.current.backgroundColor
        )
    )
}
