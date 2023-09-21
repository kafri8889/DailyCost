package com.dcns.dailycost.ui.wallets

import com.dcns.dailycost.data.WalletType

sealed interface WalletsAction {

	data class ChangeSelectedWalletType(val walletType: WalletType): WalletsAction

}