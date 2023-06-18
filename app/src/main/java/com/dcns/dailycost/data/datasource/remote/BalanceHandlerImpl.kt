package com.dcns.dailycost.data.datasource.remote

import com.dcns.dailycost.data.datasource.remote.handlers.BalanceHandler
import com.dcns.dailycost.data.datasource.remote.services.BalanceService
import com.dcns.dailycost.data.model.remote.response.BalanceResponse
import retrofit2.Response
import javax.inject.Inject

class BalanceHandlerImpl @Inject constructor(
    private val balanceService: BalanceService
): BalanceHandler {

    override suspend fun getBalance(userId: Int, token: String): Response<BalanceResponse> {
        return balanceService.getBalance(userId, token)
    }
}