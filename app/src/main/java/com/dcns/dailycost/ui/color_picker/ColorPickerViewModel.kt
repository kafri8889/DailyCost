package com.dcns.dailycost.ui.color_picker

import androidx.lifecycle.SavedStateHandle
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ColorPickerViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle
): BaseViewModel<ColorPickerState, ColorPickerAction>(savedStateHandle, ColorPickerState()) {

	override fun onAction(action: ColorPickerAction) {

	}
}