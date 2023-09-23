package com.dcns.dailycost.ui.icon_picker

import com.dcns.dailycost.data.CategoryIcon

sealed interface IconPickerAction {

	data class UpdateSelectedIcon(val icon: CategoryIcon): IconPickerAction

}