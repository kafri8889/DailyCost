package com.dcns.dailycost.ui.add_category

import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.data.model.Category
import com.dcns.dailycost.domain.use_case.CategoryUseCases
import com.dcns.dailycost.domain.util.InputActionType
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

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
						name = action.name,
						nameError = action.name.isBlank()
					)
				}
			}
			AddCategoryAction.Save -> {
				viewModelScope.launch(Dispatchers.IO) {
					val mState = state.value

					if (mState.name.isBlank()) {
						updateState {
							copy(
								nameError = true
							)
						}

						return@launch
					}

					categoryUseCases.inputLocalCategoryUseCase(
						inputActionType = InputActionType.Insert,
						Category(
							id = Random.nextInt(),
							name = mState.name,
							icon = mState.icon
						)
					)

					sendEvent(AddCategoryUiEvent.Saved())
				}
			}
		}
	}
}