package com.dcns.dailycost.data.model.networking.response

import com.dcns.dailycost.data.model.networking.BalanceResponseData

data class BalanceResponse(
    val data: BalanceResponseData,
    val status: String
)