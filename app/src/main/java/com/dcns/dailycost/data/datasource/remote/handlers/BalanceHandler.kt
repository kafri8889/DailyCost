package com.dcns.dailycost.data.datasource.remote.handlers

import com.dcns.dailycost.data.model.remote.response.BalanceResponse
import retrofit2.Response

interface BalanceHandler {

    suspend fun getBalance(
        userId: Int,
        token: String
    ): Response<BalanceResponse>

}