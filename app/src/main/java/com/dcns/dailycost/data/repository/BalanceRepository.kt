package com.dcns.dailycost.data.repository

import com.dcns.dailycost.data.datasource.remote.handlers.BalanceHandler
import com.dcns.dailycost.data.model.networking.response.BalanceResponse
import com.dcns.dailycost.domain.repository.IBalanceRepository
import retrofit2.Response
import javax.inject.Inject

class BalanceRepository @Inject constructor(
    private val balanceHandler: BalanceHandler
): IBalanceRepository {

    override suspend fun getBalance(userId: Int, token: String): Response<BalanceResponse> {
        return balanceHandler.getBalance(userId, token)
    }
}