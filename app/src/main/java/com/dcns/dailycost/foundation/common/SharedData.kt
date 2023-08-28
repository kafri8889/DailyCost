package com.dcns.dailycost.foundation.common

import com.dcns.dailycost.data.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class SharedData @Inject constructor() {

	private val _category = MutableStateFlow<Category?>(null)
	val category: StateFlow<Category?> = _category

	fun setCategory(category: Category?) {
		_category.update { category }
	}

}