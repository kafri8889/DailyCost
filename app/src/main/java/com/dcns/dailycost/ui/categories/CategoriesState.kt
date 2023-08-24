package com.dcns.dailycost.ui.categories

import com.dcns.dailycost.data.model.Category

data class CategoriesState(
	val categories: List<Category> = emptyList()
)
