package com.dcns.dailycost.ui.icon_picker

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IconPickerViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle
): BaseViewModel<IconPickerState, IconPickerAction>(savedStateHandle, IconPickerState()) {

	override fun onAction(action: IconPickerAction) {
		when (action) {
			is IconPickerAction.UpdateSelectedIcon -> {
				viewModelScope.launch(Dispatchers.IO) {
					updateState {
						copy(
							selectedIcon = action.icon
						)
					}
				}
			}
		}
	}
}