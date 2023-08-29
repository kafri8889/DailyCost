package com.dcns.dailycost.domain.use_case.expense

import com.dcns.dailycost.data.model.local.ExpenseDb
import com.dcns.dailycost.domain.repository.IExpenseRepository

/**
 * Use case untuk menghapus pengeluaran dari lokal database
 */
class DeleteLocalExpenseUseCase(
	private val expenseRepository: IExpenseRepository
) {

	suspend operator fun invoke(vararg expenseDb: ExpenseDb) {
		expenseRepository.deleteExpense(*expenseDb)
	}

}