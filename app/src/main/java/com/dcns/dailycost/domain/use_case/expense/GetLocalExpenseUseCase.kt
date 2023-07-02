package com.dcns.dailycost.domain.use_case.expense

import com.dcns.dailycost.data.model.Expense
import com.dcns.dailycost.domain.repository.IExpenseRepository
import com.dcns.dailycost.domain.util.GetExpenseBy
import com.dcns.dailycost.foundation.extension.toExpense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

/**
 * Use case untuk mendapatkan pengeluaran lokal
 */
class GetLocalExpenseUseCase(
    private val expenseRepository: IExpenseRepository
) {

    operator fun invoke(
        getExpenseBy: GetExpenseBy = GetExpenseBy.All
    ): Flow<List<Expense>> {
        val flow = when (getExpenseBy) {
            is GetExpenseBy.ID -> expenseRepository.getExpenseById(getExpenseBy.id).map { listOf(it) }
            GetExpenseBy.All -> expenseRepository.getAllExpenses()
        }

        return flow
            .filterNotNull()
            .map { it.filterNotNull().map { it.toExpense() } }
    }
}