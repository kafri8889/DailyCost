package com.dcns.dailycost.navigation.home.shared

import android.os.Parcelable
import androidx.compose.ui.graphics.toArgb
import com.dcns.dailycost.data.CategoryIcon
import com.dcns.dailycost.data.DailyCostColorPalette
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.model.Category
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeSharedState(
	/**
	 * Digunakan di destinasi [TopLevelDestinations.Home.colorPicker]
	 */
	val selectedArgbColor: Int = DailyCostColorPalette.values[0].toArgb(),
	/**
	 * Digunakan di destinasi [TopLevelDestinations.Home.transaction] dan [TopLevelDestinations.Home.categories]
	 */
	val selectedCategory: Category? = null,
	/**
	 * Digunakan di destinasi [TopLevelDestinations.Home.transaction] dan [TopLevelDestinations.Home.wallets]
	 */
	val selectedWalletType: WalletType? = null,
	/**
	 * Digunakan di destinasi [TopLevelDestinations.Home.iconPicker]
	 */
	val selectedCategoryIcon: CategoryIcon = CategoryIcon.Other
): Parcelable
