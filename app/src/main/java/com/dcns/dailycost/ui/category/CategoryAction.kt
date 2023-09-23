package com.dcns.dailycost.ui.category

import com.dcns.dailycost.data.CategoryIcon

sealed interface CategoryAction {

	data class SetName(val name: String): CategoryAction
	data class SetIcon(val icon: CategoryIcon): CategoryAction
	data class SetColor(val argb: Int): CategoryAction

	data object Save: CategoryAction

}