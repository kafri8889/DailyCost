package com.dcns.dailycost.data.model.remote

data class IncomeResponseData(
    val uang_cash: Int,
    val uang_gopay: Int,
    val uang_rekening: Int,
    val user_id: Int
)