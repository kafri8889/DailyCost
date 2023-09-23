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
import kotlinx.coroutines.flow.collectLatest
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
			}.filterNotNull().collectLatest { category ->
					Timber.i("Received category: $category")
					updateState {
						copy(
							category = category
						)
					}
				}
		}

		viewModelScope.launch(Dispatchers.IO) {
			deliveredActionMode
				.filterNotNull()
				.collectLatest { mode ->
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
							category = category.copy(
								icon = action.icon
							)
						)
					}
				}
			}
			is CategoryAction.SetName -> viewModelScope.launch {
				updateState {
					copy(
						nameError = action.name.isBlank(),
						category = category.copy(
							name = action.name
						)
					)
				}
			}
			is CategoryAction.SetColor -> viewModelScope.launch {
				updateState {
					copy(
						category = category.copy(
							colorArgb = action.argb
						)
					)
				}
			}
			CategoryAction.Save -> {
				viewModelScope.launch(Dispatchers.IO) {
					val mState = state.value

					if (mState.category.name.isBlank()) {
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
							name = mState.category.name.trim().uppercaseFirstLetterInWord(),
							icon = mState.category.icon,
							colorArgb = mState.category.colorArgb
						)
					)

					sendEvent(CategoryUiEvent.Saved())
				}
			}
		}
	}
}