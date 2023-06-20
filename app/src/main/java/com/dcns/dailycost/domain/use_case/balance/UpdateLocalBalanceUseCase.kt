package com.dcns.dailycost.domain.use_case.balance

import com.dcns.dailycost.domain.repository.IBalanceRepository

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