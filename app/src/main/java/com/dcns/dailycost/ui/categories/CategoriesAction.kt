package com.dcns.dailycost.ui.categories

import com.dcns.dailycost.data.model.Category

sealed interface CategoriesAction {

	data class ChangeSelectedCategory(val category: Category): CategoriesAction

}