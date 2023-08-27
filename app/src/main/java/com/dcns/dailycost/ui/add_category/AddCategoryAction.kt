package com.dcns.dailycost.ui.add_category

import com.dcns.dailycost.data.CategoryIcon

sealed interface AddCategoryAction {

	data class SetName(val name: String): AddCategoryAction
	data class SetIcon(val icon: CategoryIcon): AddCategoryAction

	data object Save: AddCategoryAction

}