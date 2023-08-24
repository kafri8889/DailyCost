package com.dcns.dailycost.data.model.remote

import com.google.gson.annotations.SerializedName

data class BalanceResponseData(
	@SerializedName("uang_cash") val cash: Int,
	@SerializedName("uang_gopay") val eWallet: Int,
	@SerializedName("uang_rekening") val bankAccount: Int
)