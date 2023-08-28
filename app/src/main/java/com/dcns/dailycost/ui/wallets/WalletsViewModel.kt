package com.dcns.dailycost.ui.wallets

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.data.DestinationArgument
import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.WalletsScreenMode
import com.dcns.dailycost.foundation.base.BaseViewModel
import com.dcns.dailycost.foundation.common.SharedData
import com.dcns.dailycost.foundation.extension.toWallet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletsViewModel @Inject constructor(
	private val sharedData: SharedData,
	savedStateHandle: SavedStateHandle
): BaseViewModel<WalletsState, WalletsAction>() {

	private val deliveredWalletsScreenMode =
		savedStateHandle.getStateFlow(DestinationArgument.WALLETS_SCREEN_MODE, WalletsScreenMode.WalletList)

	private val deliveredWalletId =
		savedStateHandle.getStateFlow(DestinationArgument.WALLET_ID, WalletType.Cash.ordinal)

	init {
		viewModelScope.launch {
			deliveredWalletId.collect { id ->
				updateState {
					copy(
						selectedWallet = WalletType.entries[id].toWallet(0.0)
					)
				}
			}
		}

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

	override fun defaultState(): WalletsState = WalletsState()

	override fun onAction(action: WalletsAction) {
		when (action) {
			is WalletsAction.ChangeSelectedWallet -> {
				viewModelScope.launch {
					updateState {
						copy(
							selectedWallet = action.wallet
						)
					}
				}
			}
			WalletsAction.SendWallet -> {
				sharedData.setWallet(state.value.selectedWallet)
			}
		}
	}
}