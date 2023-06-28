package com.dcns.dailycost.data.repository

import com.dcns.dailycost.data.datasource.local.dao.ExpenseDao
import com.dcns.dailycost.data.datasource.remote.handlers.ExpenseHandler
import com.dcns.dailycost.data.model.local.ExpenseDb
import com.dcns.dailycost.data.model.local.relation.ExpenseDbWithCategoryDb
import com.dcns.dailycost.data.model.remote.response.DeleteResponse
import com.dcns.dailycost.data.model.remote.response.expense.AddExpenseResponse
import com.dcns.dailycost.data.model.remote.response.expense.GetExpenseResponse
import com.dcns.dailycost.domain.repository.IExpenseRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val expenseHandler: ExpenseHandler,
    private val expenseDao: ExpenseDao
): IExpenseRepository {
    override suspend fun addRemoteExpense(
        body: RequestBody,
        token: String
    ): Response<AddExpenseResponse> {
        return expenseHandler.addExpense(body, token)
    }

    override suspend fun getRemoteExpense(userId: Int, token: String): Response<GetExpenseResponse> {
        return expenseHandler.getExpense(userId, token)
    }

    override suspend fun deleteRemoteExpense(body: RequestBody, token: String): Response<DeleteResponse> {
        return expenseHandler.deleteExpense(body, token)
    }

    override fun getAllExpenses(): Flow<List<ExpenseDbWithCategoryDb>> {
        return expenseDao.getAllExpenses()
    }

    override fun getExpenseById(id: Int): Flow<ExpenseDbWithCategoryDb?> {
        return expenseDao.getExpenseById(id)
    }

    override suspend fun deleteExpenseExcept(ids: List<Int>) {
        expenseDao.deleteExpenseExcept(ids)
    }

    override suspend fun updateExpense(vararg expense: ExpenseDb) {
        expenseDao.updateExpense(*expense)
    }

    override suspend fun upsertExpense(vararg expense: ExpenseDb) {
        expenseDao.upsertExpense(*expense)
    }

    override suspend fun deleteExpense(vararg expense: ExpenseDb) {
        expenseDao.deleteExpense(*expense)
    }

    override suspend fun insertExpense(vararg expense: ExpenseDb) {
        expenseDao.insertExpense(*expense)
    }

}