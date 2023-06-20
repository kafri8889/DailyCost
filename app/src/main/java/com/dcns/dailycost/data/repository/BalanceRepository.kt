package com.dcns.dailycost.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import com.dcns.dailycost.ProtoUserBalance
import com.dcns.dailycost.data.datasource.remote.handlers.BalanceHandler
import com.dcns.dailycost.data.model.UserBalance
import com.dcns.dailycost.data.model.remote.response.BalanceResponse
import com.dcns.dailycost.domain.repository.IBalanceRepository
import com.dcns.dailycost.foundation.extension.toUserBalance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject

class BalanceRepository @Inject constructor(
    private val balanceHandler: BalanceHandler,
    private val balanceDataStore: DataStore<ProtoUserBalance>
): IBalanceRepository {

    override val getUserBalance: Flow<UserBalance>
        get() = balanceDataStore.data
            .map { it.toUserBalance() }

    override suspend fun setCash(value: Double) {
        balanceDataStore.updateData {
            it.copy(
                cash = value
            )
        }
    }

    override suspend fun setEWallet(value: Double) {
        balanceDataStore.updateData {
            it.copy(
                eWallet = value
            )
        }
    }

    override suspend fun setBankAccount(value: Double) {
        balanceDataStore.updateData {
            it.copy(
                bankAccount = value
            )
        }
    }

    override suspend fun getRemoteBalance(userId: Int, token: String): Response<BalanceResponse> {
        return balanceHandler.getBalance(userId, token)
    }

    companion object {
        val corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { ProtoUserBalance() }
        )
    }
}