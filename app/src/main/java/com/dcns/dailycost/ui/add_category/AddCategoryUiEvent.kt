package com.dcns.dailycost.ui.add_category

import android.widget.Toast
import com.dcns.dailycost.R
import com.dcns.dailycost.foundation.base.UiEvent

sealed class AddCategoryUiEvent {

	class Saved(
		override val message: String = asStringResource(R.string.category_saved),
		override val length: Int = Toast.LENGTH_SHORT
	): UiEvent.ShowToast(message, length)

}