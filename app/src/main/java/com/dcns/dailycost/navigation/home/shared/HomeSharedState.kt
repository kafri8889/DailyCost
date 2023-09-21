package com.dcns.dailycost.navigation.home.shared

import android.os.Parcelable
import androidx.compose.ui.graphics.toArgb
import com.dcns.dailycost.data.DailyCostColorPalette
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeSharedState(
	val selectedArgbColor: Int = DailyCostColorPalette.values[0].toArgb()
): Parcelable
