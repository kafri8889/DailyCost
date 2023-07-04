package com.dcns.dailycost.domain.use_case.income

import com.dcns.dailycost.data.model.Income
import com.dcns.dailycost.domain.repository.IIncomeRepository
import com.dcns.dailycost.domain.util.GetTransactionBy
import com.dcns.dailycost.foundation.extension.toIncome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

/**
 * Use case untuk mendapatkan pemasukan lokal
 */
class GetLocalIncomeUseCase(
    private val incomeRepository: IIncomeRepository
) {

    operator fun invoke(
        getTransactionBy: GetTransactionBy = GetTransactionBy.All
    ): Flow<List<Income>> {
        val flow = when (getTransactionBy) {
            is GetTransactionBy.ID -> incomeRepository.getIncomeById(getTransactionBy.id).map { listOf(it) }
            GetTransactionBy.All -> incomeRepository.getAllIncomes()
        }

        return flow
            .filterNotNull()
            .map { it.filterNotNull().map { it.toIncome() } }
    }
}