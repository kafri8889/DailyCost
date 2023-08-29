package com.dcns.dailycost.ui.wallets

import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.WalletsScreenMode
import com.dcns.dailycost.data.model.Wallet
import com.dcns.dailycost.foundation.extension.toWallet

data class WalletsState(
	val wallets: List<Wallet> = WalletType.entries.map { it.toWallet(0.0) },
	val screenMode: WalletsScreenMode = WalletsScreenMode.WalletList,

	// For [WalletsScreenMode.SelectWallet]
	val selectedWallet: Wallet = WalletType.Cash.toWallet(0.0)
)
