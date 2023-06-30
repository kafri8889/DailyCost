package com.dcns.dailycost.domain.repository

import com.dcns.dailycost.data.model.UserBalance
import com.dcns.dailycost.data.model.remote.response.DepoResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface IBalanceRepository {

    val getUserBalance: Flow<UserBalance>

    /**
     * Set local cash balance
     */
    suspend fun setCash(value: Double)

    /**
     * Set local e-wallet balance
     */
    suspend fun setEWallet(value: Double)

    /**
     * Set local bank account (rekening) balance
     */
    suspend fun setBankAccount(value: Double)

    /**
     * get remote user balance
     */
    suspend fun getRemoteBalance(
        userId: Int,
        token: String
    ): Response<DepoResponse>

}