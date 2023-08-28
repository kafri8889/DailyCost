package com.dcns.dailycost.ui.wallets

import com.dcns.dailycost.data.model.Wallet

sealed interface WalletsAction {

	data class ChangeSelectedWallet(val wallet: Wallet): WalletsAction
	data object SendWallet: WalletsAction

}