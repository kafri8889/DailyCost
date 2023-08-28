package com.dcns.dailycost.foundation.extension

import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.model.Wallet

fun WalletType.toWallet(balance: Double): Wallet {
	return Wallet(this, balance)
}
