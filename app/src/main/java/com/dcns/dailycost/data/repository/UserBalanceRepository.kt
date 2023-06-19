package com.dcns.dailycost.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import com.dcns.dailycost.ProtoUserBalance
import com.dcns.dailycost.data.model.UserBalance
import com.dcns.dailycost.domain.repository.IUserBalanceRepository
import com.dcns.dailycost.foundation.extension.toUserBalance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserBalanceRepository @Inject constructor(
    private val userBalanceDataStore: DataStore<ProtoUserBalance>
): IUserBalanceRepository {

    override val getUserBalance: Flow<UserBalance>
        get() = userBalanceDataStore.data
            .map { it.toUserBalance() }

    override suspend fun setCash(value: Double) {
        userBalanceDataStore.updateData {
            it.copy(
                cash = value
            )
        }
    }

    override suspend fun setEWallet(value: Double) {
        userBalanceDataStore.updateData {
            it.copy(
                eWallet = value
            )
        }
    }

    override suspend fun setBankAccount(value: Double) {
        userBalanceDataStore.updateData {
            it.copy(
                bankAccount = value
            )
        }
    }

    companion object {
        val corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { ProtoUserBalance() }
        )
    }
}