package com.dcns.dailycost.ui.category

import android.os.Parcelable
import com.dcns.dailycost.data.ActionMode
import com.dcns.dailycost.data.CategoryIcon
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryState(
	val id: Int = -1,
	val name: String = "",
	val icon: CategoryIcon = CategoryIcon.Other,
	val default: Boolean = false,

	val actionMode: ActionMode = ActionMode.New,
	val nameError: Boolean = false
): Parcelable
