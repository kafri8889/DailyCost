package com.dcns.dailycost.ui.categories

import android.os.Parcelable
import com.dcns.dailycost.data.CategoriesScreenMode
import com.dcns.dailycost.data.datasource.local.LocalCategoryDataProvider
import com.dcns.dailycost.data.model.Category
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoriesState(
	val categories: List<Category> = emptyList(),
	val screenMode: CategoriesScreenMode = CategoriesScreenMode.CategoryList,

	// For [CategoriesScreenMode.SelectCategory]
	val selectedCategory: Category = LocalCategoryDataProvider.other
): Parcelable
