package com.dcns.dailycost.data.model.remote

import com.google.gson.annotations.SerializedName

data class ExpenseResponseData(
    @SerializedName("pengeluaran") val expenseBalance: ExpenseResponseBalance,
    @SerializedName("results") val results: List<ExpenseResponseResult>,
)
