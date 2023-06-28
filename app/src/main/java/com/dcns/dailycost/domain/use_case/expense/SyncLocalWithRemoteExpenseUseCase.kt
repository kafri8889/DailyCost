package com.dcns.dailycost.domain.use_case.expense

import com.dcns.dailycost.data.model.Expense
import com.dcns.dailycost.domain.repository.IExpenseRepository
import com.dcns.dailycost.foundation.extension.toExpenseDb

class SyncLocalWithRemoteExpenseUseCase(
    private val expenseRepository: IExpenseRepository
) {

    suspend operator fun invoke(
        remoteExpenses: List<Expense>
    ) {
        expenseRepository.upsertExpense(
            *remoteExpenses
                .map { it.toExpenseDb() }
                .toTypedArray()
        )

        expenseRepository.deleteExpenseExcept(
            remoteExpenses
                .map { it.id }
        )
    }

}