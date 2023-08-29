package com.dcns.dailycost.ui.categories

import com.dcns.dailycost.data.CategoriesScreenMode
import com.dcns.dailycost.data.datasource.local.LocalCategoryDataProvider
import com.dcns.dailycost.data.model.Category

data class CategoriesState(
	val categories: List<Category> = emptyList(),
	val screenMode: CategoriesScreenMode = CategoriesScreenMode.CategoryList,

	// For [CategoriesScreenMode.SelectCategory]
	val selectedCategory: Category = LocalCategoryDataProvider.other
)
