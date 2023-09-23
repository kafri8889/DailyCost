package com.dcns.dailycost.navigation.home.shared

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeSharedViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle
): BaseViewModel<HomeSharedState, HomeSharedAction>(savedStateHandle, HomeSharedState()) {

	override fun onAction(action: HomeSharedAction) {
		when (action) {
			is HomeSharedAction.UpdateSelectedArgbColor -> {
				viewModelScope.launch(Dispatchers.IO) {
					updateState {
						copy(
							selectedArgbColor = action.argb
						)
					}
				}
			}
			is HomeSharedAction.UpdateSelectedCategory -> {
				viewModelScope.launch(Dispatchers.IO) {
					updateState {
						copy(
							selectedCategory = action.category
						)
					}
				}
			}
			is HomeSharedAction.UpdateSelectedWalletType -> {
				viewModelScope.launch(Dispatchers.IO) {
					updateState {
						copy(
							selectedWalletType = action.walletType
						)
					}
				}
			}
			is HomeSharedAction.UpdateSelectedCategoryIcon -> {
				viewModelScope.launch(Dispatchers.IO) {
					updateState {
						copy(
							selectedCategoryIcon = action.categoryIcon
						)
					}
				}
			}
		}
	}
}