package com.dcns.dailycost.data.repository

import com.dcns.dailycost.data.datasource.local.dao.IncomeDao
import com.dcns.dailycost.data.datasource.remote.handlers.IncomeHandler
import com.dcns.dailycost.data.model.local.IncomeDb
import com.dcns.dailycost.data.model.local.relation.IncomeDbWithCategoryDb
import com.dcns.dailycost.data.model.remote.response.DeleteResponse
import com.dcns.dailycost.data.model.remote.response.IncomeGetResponse
import com.dcns.dailycost.data.model.remote.response.IncomePostResponse
import com.dcns.dailycost.domain.repository.IIncomeRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class IncomeRepository @Inject constructor(
    private val incomeHandler: IncomeHandler,
    private val incomeDao: IncomeDao
): IIncomeRepository {

    override suspend fun addRemoteIncome(
        userId: Int,
        body: RequestBody,
        token: String
    ): Response<IncomePostResponse> {
        return incomeHandler.addIncome(userId, body, token)
    }

    override suspend fun getRemoteIncome(userId: Int, token: String): Response<IncomeGetResponse> {
        return incomeHandler.getIncome(userId, token)
    }

    override suspend fun deleteRemoteIncome(
        userId: Int,
        body: RequestBody,
        token: String
    ): Response<DeleteResponse> {
        return incomeHandler.deleteIncome(userId, body, token)
    }

    override fun getAllIncomes(): Flow<List<IncomeDbWithCategoryDb>> {
        return incomeDao.getAllIncomes()
    }

    override fun getIncomeById(id: Int): Flow<IncomeDbWithCategoryDb?> {
        return incomeDao.getIncomeById(id)
    }

    override suspend fun deleteIncomeExcept(ids: List<Int>) {
        return incomeDao.deleteIncomeExcept(ids)
    }

    override suspend fun updateIncome(vararg income: IncomeDb) {
        incomeDao.updateIncome(*income)
    }

    override suspend fun upsertIncome(vararg income: IncomeDb) {
        incomeDao.upsertIncome(*income)
    }

    override suspend fun deleteIncome(vararg income: IncomeDb) {
        incomeDao.deleteIncome(*income)
    }

    override suspend fun insertIncome(vararg income: IncomeDb) {
        incomeDao.insertIncome(*income)
    }
}