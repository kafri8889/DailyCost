package com.dcns.dailycost.domain.repository

import com.dcns.dailycost.data.model.local.IncomeDb
import com.dcns.dailycost.data.model.remote.response.IncomeGetResponse
import com.dcns.dailycost.data.model.remote.response.IncomePostResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import retrofit2.Response

interface IIncomeRepository {

    suspend fun addRemoteIncome(
        body: RequestBody,
        token: String
    ): Response<IncomePostResponse>

    suspend fun getRemoteIncome(
        userId: Int,
        token: String
    ): Response<IncomeGetResponse>

    fun getAllIncomes(): Flow<List<IncomeDb>>

    fun getIncomeById(id: Int): Flow<IncomeDb?>

    suspend fun updateIncome(vararg income: IncomeDb)

    suspend fun upsertIncome(vararg income: IncomeDb)

    suspend fun deleteIncome(vararg income: IncomeDb)

    suspend fun insertIncome(vararg income: IncomeDb)

}