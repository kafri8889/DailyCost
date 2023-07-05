package com.dcns.dailycost.foundation.extension

import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import com.dcns.dailycost.foundation.theme.DailyCostTheme

@Composable
fun OutlinedTextFieldDefaults.dailyCostColor(): TextFieldColors {
    return colors(
        focusedBorderColor = DailyCostTheme.colorScheme.primary,
        cursorColor = DailyCostTheme.colorScheme.primary,
        focusedLabelColor = DailyCostTheme.colorScheme.primary,
        unfocusedBorderColor = DailyCostTheme.colorScheme.outline,
        selectionColors = TextSelectionColors(
            handleColor = DailyCostTheme.colorScheme.primary,
            backgroundColor = LocalTextSelectionColors.current.backgroundColor
        )
    )
}
