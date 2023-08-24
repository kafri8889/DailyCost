package com.dcns.dailycost.domain.use_case

import com.dcns.dailycost.domain.use_case.income.AddRemoteIncomeUseCase
import com.dcns.dailycost.domain.use_case.income.DeleteRemoteIncomeUseCase
import com.dcns.dailycost.domain.use_case.income.GetLocalIncomeUseCase
import com.dcns.dailycost.domain.use_case.income.GetRemoteIncomeUseCase
import com.dcns.dailycost.domain.use_case.income.SyncLocalWithRemoteIncomeUseCase

data class IncomeUseCases(
	val addRemoteIncomeUseCase: AddRemoteIncomeUseCase,
	val deleteRemoteIncomeUseCase: DeleteRemoteIncomeUseCase,
	val getRemoteIncomeUseCase: GetRemoteIncomeUseCase,
	val getLocalIncomeUseCase: GetLocalIncomeUseCase,
	val syncLocalWithRemoteIncomeUseCase: SyncLocalWithRemoteIncomeUseCase
)
