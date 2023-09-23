package com.dcns.dailycost.navigation.home.shared

import com.dcns.dailycost.data.CategoryIcon
import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.model.Category

sealed interface HomeSharedAction {

	data class UpdateSelectedArgbColor(val argb: Int): HomeSharedAction
	data class UpdateSelectedCategory(val category: Category?): HomeSharedAction
	data class UpdateSelectedWalletType(val walletType: WalletType?): HomeSharedAction
	data class UpdateSelectedCategoryIcon(val categoryIcon: CategoryIcon): HomeSharedAction

}