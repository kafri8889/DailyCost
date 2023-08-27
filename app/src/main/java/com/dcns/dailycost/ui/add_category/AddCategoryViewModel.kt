package com.dcns.dailycost.ui.add_category

import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.domain.use_case.CategoryUseCases
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
	private val categoryUseCases: CategoryUseCases
): BaseViewModel<AddCategoryState, AddCategoryAction>() {

	override fun defaultState(): AddCategoryState = AddCategoryState()

	override fun onAction(action: AddCategoryAction) {
		when (action) {
			is AddCategoryAction.SetIcon -> {
				viewModelScope.launch {
					updateState {
						copy(
							icon = action.icon
						)
					}
				}
			}
			is AddCategoryAction.SetName -> viewModelScope.launch {
				updateState {
					copy(
						name = action.name
					)
				}
			}
			AddCategoryAction.Save -> {
				viewModelScope.launch {

				}
			}
		}
	}
}