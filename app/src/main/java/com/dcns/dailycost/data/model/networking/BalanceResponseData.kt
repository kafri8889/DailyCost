package com.dcns.dailycost.data.model.networking

import com.google.gson.annotations.SerializedName

data class BalanceResponseData(
    @SerializedName("uang_cash") val cash: Int,
    @SerializedName("uang_gopay") val gopay: Int,
    @SerializedName("uang_rekening") val bankAccount: Int
)