package com.dcns.dailycost.domain.repository

import com.dcns.dailycost.data.model.UserBalance
import com.dcns.dailycost.data.model.remote.response.BalanceResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface IBalanceRepository {

    val getUserBalance: Flow<UserBalance>

    suspend fun setCash(value: Double)
    suspend fun setEWallet(value: Double)
    suspend fun setBankAccount(value: Double)

    suspend fun getRemoteBalance(
        userId: Int,
        token: String
    ): Response<BalanceResponse>

}