package com.dcns.dailycost.data.model

import android.os.Parcelable
import com.dcns.dailycost.data.WalletType
import kotlinx.parcelize.Parcelize

@Parcelize
data class Wallet(
	val walletType: WalletType,
	val amount: Double
): Parcelable
