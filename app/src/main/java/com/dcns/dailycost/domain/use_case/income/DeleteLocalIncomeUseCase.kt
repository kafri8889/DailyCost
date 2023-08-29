package com.dcns.dailycost.domain.use_case.income

import com.dcns.dailycost.data.model.local.IncomeDb
import com.dcns.dailycost.domain.repository.IIncomeRepository

/**
 * Use case untuk menghapus pemasukan dari lokal database
 */
class DeleteLocalIncomeUseCase(
	private val incomeRepository: IIncomeRepository
) {

	suspend operator fun invoke(vararg incomeDb: IncomeDb) {
		incomeRepository.deleteIncome(*incomeDb)
	}

}