package com.dcns.dailycost.domain.use_case

import com.dcns.dailycost.domain.use_case.expense.AddRemoteExpenseUseCase
import com.dcns.dailycost.domain.use_case.expense.DeleteLocalExpenseUseCase
import com.dcns.dailycost.domain.use_case.expense.DeleteRemoteExpenseUseCase
import com.dcns.dailycost.domain.use_case.expense.GetLocalExpenseUseCase
import com.dcns.dailycost.domain.use_case.expense.GetRemoteExpenseUseCase
import com.dcns.dailycost.domain.use_case.expense.SyncLocalWithRemoteExpenseUseCase

data class ExpenseUseCases(
	val addRemoteExpenseUseCase: AddRemoteExpenseUseCase,
	val deleteRemoteExpenseUseCase: DeleteRemoteExpenseUseCase,
	val deleteLocalExpenseUseCase: DeleteLocalExpenseUseCase,
	val getRemoteExpenseUseCase: GetRemoteExpenseUseCase,
	val getLocalExpenseUseCase: GetLocalExpenseUseCase,
	val syncLocalWithRemoteExpenseUseCase: SyncLocalWithRemoteExpenseUseCase
)