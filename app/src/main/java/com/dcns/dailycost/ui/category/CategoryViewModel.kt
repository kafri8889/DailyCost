package com.dcns.dailycost.ui.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.data.ActionMode
import com.dcns.dailycost.data.DestinationArgument
import com.dcns.dailycost.data.model.Category
import com.dcns.dailycost.domain.use_case.CategoryUseCases
import com.dcns.dailycost.domain.util.GetCategoryBy
import com.dcns.dailycost.domain.util.InputActionType
import com.dcns.dailycost.foundation.base.BaseViewModel
import com.dcns.dailycost.foundation.extension.uppercaseFirstLetterInWord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CategoryViewModel @Inject constructor(
	private val categoryUseCases: CategoryUseCases,
	savedStateHandle: SavedStateHandle
): BaseViewModel<CategoryState, CategoryAction>(savedStateHandle, CategoryState()) {

	private val deliveredCategoryId =
		savedStateHandle.getStateFlow<Int?>(DestinationArgument.CATEGORY_ID, null)
	private val deliveredActionMode =
		savedStateHandle.getStateFlow<ActionMode?>(DestinationArgument.ACTION_MODE, null)

	init {
		viewModelScope.launch(Dispatchers.IO) {
			deliveredCategoryId.filterNotNull().flatMapLatest { id ->
				categoryUseCases.getLocalCategoryUseCase(
					getCategoryBy = GetCategoryBy.ID(id)
				).map { it.getOrNull(0) }
			}.filterNotNull().collect { category ->
					Timber.i("Received category: $category")
					updateState {
						copy(
							id = category.id,
							name = category.name,
							icon = category.icon,
							default = category.defaultCategory
						)
					}
				}
		}

		viewModelScope.launch(Dispatchers.IO) {
			deliveredActionMode
				.filterNotNull()
				.collect { mode ->
					updateState {
						copy(
							actionMode = mode
						)
					}
				}
		}
	}

	override fun onAction(action: CategoryAction) {
		when (action) {
			is CategoryAction.SetIcon -> {
				viewModelScope.launch {
					updateState {
						copy(
							icon = action.icon
						)
					}
				}
			}
			is CategoryAction.SetName -> viewModelScope.launch {
				updateState {
					copy(
						name = action.name,
						nameError = action.name.isBlank()
					)
				}
			}
			CategoryAction.Save -> {
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
							name = mState.name.trim().uppercaseFirstLetterInWord(),
							icon = mState.icon
						)
					)

					sendEvent(CategoryUiEvent.Saved())
				}
			}
		}
	}
}