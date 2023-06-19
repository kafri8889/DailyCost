package com.dcns.dailycost.domain.repository

import com.dcns.dailycost.data.model.UserBalance
import kotlinx.coroutines.flow.Flow

interface IUserBalanceRepository {

    val getUserBalance: Flow<UserBalance>

    suspend fun setCash(value: Double)
    suspend fun setEWallet(value: Double)
    suspend fun setBankAccount(value: Double)

}