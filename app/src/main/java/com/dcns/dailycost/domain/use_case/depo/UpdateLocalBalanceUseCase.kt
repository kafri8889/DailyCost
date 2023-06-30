package com.dcns.dailycost.domain.use_case.depo

import com.dcns.dailycost.domain.repository.IBalanceRepository

/**
 * Use case untuk memperbarui saldo lokal
 */
class UpdateLocalBalanceUseCase(
    private val balanceRepository: IBalanceRepository
) {

    suspend operator fun invoke(
        cash: Double,
        eWallet: Double,
        bankAccount: Double
    ) {
        with(balanceRepository) {
            setCash(cash)
            setEWallet(eWallet)
            setBankAccount(bankAccount)
        }
    }

}