package com.dcns.dailycost.domain.use_case.combined

import com.anafthdev.datemodule.infix_function.inSameMonth
import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.model.Balance
import com.dcns.dailycost.domain.repository.IBalanceRepository
import com.dcns.dailycost.domain.repository.IExpenseRepository
import com.dcns.dailycost.foundation.extension.toExpense
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest

/**
 * Use case untuk mendapatkan [Balance] yg digunakan di dashboard balance card
 */
class GetBalanceUseCase(
	private val expenseRepository: IExpenseRepository,
	private val balanceRepository: IBalanceRepository
) {

	@OptIn(ExperimentalCoroutinesApi::class)
	operator fun invoke(): Flow<List<Balance>> {
		return combine(
			balanceRepository.getUserBalance,
			expenseRepository.getAllExpenses()
		) { userBalance, expenseDbWithCategoryDbList ->
			userBalance to expenseDbWithCategoryDbList
		}.mapLatest { (userBalance, expenseDbWithCategoryDbList) ->
			val expenses = expenseDbWithCategoryDbList.map { it.toExpense() }

			val cashMonthlyExpenses = expenses.asSequence() // Convert ke sequence
				// Filter jika payment == cash dan tanggal berada di bulan yg sama
				.filter { it.payment == WalletType.Cash && it.date inSameMonth System.currentTimeMillis() }
				.sumOf { it.amount }

			val ewalletMonthlyExpenses = expenses.asSequence() // Convert ke sequence
				// Filter jika payment == e-wallet dan tanggal berada di bulan yg sama
				.filter { it.payment == WalletType.EWallet && it.date inSameMonth System.currentTimeMillis() }
				.sumOf { it.amount }

			val bankAccountMonthlyExpenses = expenses.asSequence() // Convert ke sequence
				// Filter jika payment == e-wallet dan tanggal berada di bulan yg sama
				.filter { it.payment == WalletType.BankAccount && it.date inSameMonth System.currentTimeMillis() }
				.sumOf { it.amount }

			listOf(
				Balance(
					amount = userBalance.cash,
					walletType = WalletType.Cash,
					monthlyExpense = cashMonthlyExpenses
				),
				Balance(
					amount = userBalance.eWallet,
					walletType = WalletType.EWallet,
					monthlyExpense = ewalletMonthlyExpenses
				),
				Balance(
					amount = userBalance.bankAccount,
					walletType = WalletType.BankAccount,
					monthlyExpense = bankAccountMonthlyExpenses
				)
			)
		}
	}

}