package com.dcns.dailycost.data.model.remote

import com.google.gson.annotations.SerializedName

data class ExpenseResponseBalance(
    @SerializedName("pengeluaran_cash") val cash: Int,
    @SerializedName("pengeluaran_gopay") val eWallet: Int,
    @SerializedName("pengeluaran_rekening") val bankAccount: Int
)