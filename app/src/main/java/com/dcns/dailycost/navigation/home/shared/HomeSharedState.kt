package com.dcns.dailycost.navigation.home.shared

import android.os.Parcelable
import androidx.compose.ui.graphics.toArgb
import com.dcns.dailycost.data.DailyCostColorPalette
import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.model.Category
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeSharedState(
	val selectedArgbColor: Int = DailyCostColorPalette.values[0].toArgb(),
	val selectedCategory: Category? = null,
	val selectedWalletType: WalletType? = null
): Parcelable
