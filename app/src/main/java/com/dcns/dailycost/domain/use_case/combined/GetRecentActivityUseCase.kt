package com.dcns.dailycost.domain.use_case.combined

import com.dcns.dailycost.data.model.Expense
import com.dcns.dailycost.data.model.Income
import com.dcns.dailycost.data.model.Note
import com.dcns.dailycost.domain.repository.IExpenseRepository
import com.dcns.dailycost.domain.repository.IIncomeRepository
import com.dcns.dailycost.domain.repository.INoteRepository
import com.dcns.dailycost.foundation.common.SortableByDate
import com.dcns.dailycost.foundation.extension.toExpense
import com.dcns.dailycost.foundation.extension.toIncome
import com.dcns.dailycost.foundation.extension.toNote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlin.math.max

/**
 * Use case untuk mendapatkan aktivitas terbaru [Note], [Expense], [Income]
 */
class GetRecentActivityUseCase(
	private val expenseRepository: IExpenseRepository,
	private val incomeRepository: IIncomeRepository,
	private val noteRepository: INoteRepository
) {

	/**
	 * List can contain [Note], [Expense], or [Income]
	 *
	 * @param n take [n] first elements
	 */
	operator fun invoke(
		n: Int = 5
	): Flow<List<Any>> {
		return combine(
			expenseRepository.getAllExpenses(),
			incomeRepository.getAllIncomes(),
			noteRepository.getAllLocalNote()
		) { expenses, incomes, notes ->
			arrayListOf<SortableByDate>().apply {
				for (i in 0 until max(max(expenses.size, incomes.size), notes.size)) {
					expenses.getOrNull(i)?.let { add(it.toExpense()) }
					incomes.getOrNull(i)?.let { add(it.toIncome()) }
					notes.getOrNull(i)?.let { add(it.toNote()) }
				}
			}.sortedByDescending { it.date }.take(n)
		}
	}

}