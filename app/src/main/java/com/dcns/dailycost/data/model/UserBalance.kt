package com.dcns.dailycost.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserBalance(
	val cash: Double,
	val eWallet: Double,
	val bankAccount: Double
): Parcelable
