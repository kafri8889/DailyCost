package com.dcns.dailycost.foundation.extension

import com.dcns.dailycost.data.model.Category
import com.dcns.dailycost.data.model.local.CategoryDb

fun CategoryDb.toCategory(): Category {
	return Category(id, name, icon, color, defaultCategory)
}

fun Category.toCategoryDb(): CategoryDb {
	return CategoryDb(id, name, icon, colorArgb, defaultCategory)
}
