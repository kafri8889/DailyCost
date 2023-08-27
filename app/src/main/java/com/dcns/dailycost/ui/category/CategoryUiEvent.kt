package com.dcns.dailycost.ui.category

import android.widget.Toast
import com.dcns.dailycost.R
import com.dcns.dailycost.foundation.base.UiEvent

sealed class CategoryUiEvent {

	class Saved(
		override val message: String = asStringResource(R.string.category_saved),
		override val length: Int = Toast.LENGTH_SHORT
	): UiEvent.ShowToast(message, length)

}