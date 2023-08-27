package com.dcns.dailycost.ui.add_category

import com.dcns.dailycost.data.CategoryIcon

data class AddCategoryState(
	val name: String = "",
	val icon: CategoryIcon = CategoryIcon.Other
)
