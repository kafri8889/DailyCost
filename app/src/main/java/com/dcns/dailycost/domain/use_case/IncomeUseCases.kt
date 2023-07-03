package com.dcns.dailycost.domain.use_case

import com.dcns.dailycost.domain.use_case.income.AddRemoteIncomeUseCase
import com.dcns.dailycost.domain.use_case.income.GetLocalIncomeUseCase
import com.dcns.dailycost.domain.use_case.income.GetRemoteIncomeUseCase

data class IncomeUseCases(
    val addRemoteIncomeUseCase: AddRemoteIncomeUseCase,
    val getRemoteIncomeUseCase: GetRemoteIncomeUseCase,
    val getLocalIncomeUseCase: GetLocalIncomeUseCase
)
