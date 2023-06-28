package com.dcns.dailycost.data.model.remote

import com.google.gson.annotations.SerializedName

data class GetExpenseResponseData(
    @SerializedName("pengeluaran") val expenseBalance: GetExpenseResponseBalance,
    @SerializedName("results") val results: List<GetExpenseResponseResult>,
)
