package com.dcns.dailycost.ui.color_picker

import android.os.Parcelable
import androidx.compose.ui.graphics.toArgb
import com.dcns.dailycost.data.DailyCostColorPalette
import kotlinx.parcelize.Parcelize

@Parcelize
data class ColorPickerState(
	val argbColors: List<Int> = DailyCostColorPalette.values.map { it.toArgb() }
): Parcelable
