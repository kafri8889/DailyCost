package com.dcns.dailycost.domain.use_case.income

import com.dcns.dailycost.data.model.Income
import com.dcns.dailycost.domain.repository.IIncomeRepository
import com.dcns.dailycost.foundation.extension.toIncomeDb
import timber.log.Timber

/**
 * Use case untuk mengsinkronisasikan pengeluaran dari remote ke lokal
 */
class SyncLocalWithRemoteIncomeUseCase(
	private val incomeRepository: IIncomeRepository
) {

	suspend operator fun invoke(
		remoteIncomes: List<Income>
	) {
		// Perbarui income lokal dengan income remote
		// Jika income dari remote ada di income lokal (id-nya sama)
		// Update income lokal dengan data dari income remote
		// Jika tidak (id dari remote tidak ada di lokal)
		// Insert income dari remote ke lokal
		Timber.i("remote incomes: $remoteIncomes")
		incomeRepository.upsertIncome(
			*remoteIncomes
				.map { it.toIncomeDb() }
				.toTypedArray()
		)

		// Hapus semua income di database lokal
		// Kecuali income yg id-nya ada di [remoteIncomes]
		incomeRepository.deleteIncomeExcept(
			remoteIncomes
				.map { it.id }
		)
	}

}