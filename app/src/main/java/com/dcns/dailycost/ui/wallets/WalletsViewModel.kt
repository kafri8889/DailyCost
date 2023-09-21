package com.dcns.dailycost.ui.wallets

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.data.DestinationArgument
import com.dcns.dailycost.data.WalletsScreenMode
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletsViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle
): BaseViewModel<WalletsState, WalletsAction>(savedStateHandle, WalletsState()) {

	private val deliveredWalletsScreenMode =
		savedStateHandle.getStateFlow(DestinationArgument.WALLETS_SCREEN_MODE, WalletsScreenMode.WalletList)

	init {
		viewModelScope.launch {
			deliveredWalletsScreenMode.collect { mode ->
				updateState {
					copy(
						screenMode = mode
					)
				}
			}
		}
	}

	override fun onAction(action: WalletsAction) {
		when (action) {
			is WalletsAction.ChangeSelectedWalletType -> {
				viewModelScope.launch {
					updateState {
						copy(
							selectedWalletType = action.walletType
						)
					}
				}
			}
		}
	}
}