package com.dcns.dailycost.foundation.extension

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.dcns.dailycost.data.model.Category
import com.dcns.dailycost.data.model.local.CategoryDb

fun CategoryDb.toCategory(): Category {
	return Category(id, name, icon, Color(color), defaultCategory)
}

fun Category.toCategoryDb(): CategoryDb {
	return CategoryDb(id, name, icon, color.toArgb(), defaultCategory)
}
