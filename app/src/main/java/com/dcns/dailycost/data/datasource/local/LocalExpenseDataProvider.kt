package com.dcns.dailycost.data.datasource.local

import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.model.Expense

/**
 * Data provider lokal untuk expense (berguna untuk testing)
 */
object LocalExpenseDataProvider {

	val expense1 = Expense(
		id = 0,
		userId = 73,
		name = "Expense 1",
		amount = 0.0,
		payment = WalletType.Cash,
		date = System.currentTimeMillis(),
		category = LocalCategoryDataProvider.Expense.entertainment
	)

	val expense2 = Expense(
		id = 1,
		userId = 73,
		name = "Expense 2",
		amount = 1_000_000.0,
		payment = WalletType.EWallet,
		date = System.currentTimeMillis(),
		category = LocalCategoryDataProvider.Expense.electronic
	)

	val values = arrayOf(
		expense1,
		expense2
	)

}