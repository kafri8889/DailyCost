package com.dcns.dailycost.domain.use_case.expense

import com.dcns.dailycost.data.model.Expense
import com.dcns.dailycost.domain.repository.IExpenseRepository
import com.dcns.dailycost.foundation.extension.toExpenseDb
import timber.log.Timber

/**
 * Use case untuk mengsinkronisasikan pengeluaran dari remote ke lokal
 */
class SyncLocalWithRemoteExpenseUseCase(
    private val expenseRepository: IExpenseRepository
) {

    suspend operator fun invoke(
        remoteExpenses: List<Expense>
    ) {
        // Perbarui expense lokal dengan expense remote
        // Jika expense dari remote ada di expense lokal (id-nya sama)
        // Update expense lokal dengan data dari expense remote
        // Jika tidak (id dari remote tidak ada di lokal)
        // Insert expense dari remote ke lokal
        Timber.i("remote expenses: $remoteExpenses")
        expenseRepository.upsertExpense(
            *remoteExpenses
                .map { it.toExpenseDb() }
                .toTypedArray()
        )

        // Hapus semua expense di database lokal
        // Kecuali expense yg id-nya ada di [remoteExpenses]
        expenseRepository.deleteExpenseExcept(
            remoteExpenses
                .map { it.id }
        )
    }

}