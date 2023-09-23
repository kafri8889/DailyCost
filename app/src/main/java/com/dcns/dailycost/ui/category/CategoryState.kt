package com.dcns.dailycost.ui.category

import android.os.Parcelable
import com.dcns.dailycost.data.ActionMode
import com.dcns.dailycost.data.datasource.local.LocalCategoryDataProvider
import com.dcns.dailycost.data.model.Category
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryState(
	val category: Category = LocalCategoryDataProvider.Unspecified,

	val actionMode: ActionMode = ActionMode.New,
	val nameError: Boolean = false
): Parcelable
