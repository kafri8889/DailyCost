package com.dcns.dailycost.domain.repository

import com.dcns.dailycost.data.model.networking.response.BalanceResponse
import retrofit2.Response

interface IBalanceRepository {

    suspend fun getBalance(
        userId: Int,
        token: String
    ): Response<BalanceResponse>

}