package com.dcns.dailycost.ui.icon_picker

import android.os.Parcelable
import com.dcns.dailycost.data.CategoryIcon
import kotlinx.parcelize.Parcelize

@Parcelize
data class IconPickerState(
	val selectedIcon: CategoryIcon = CategoryIcon.Other
): Parcelable
