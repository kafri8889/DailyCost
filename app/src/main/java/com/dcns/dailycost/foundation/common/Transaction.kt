package com.dcns.dailycost.foundation.common

import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.model.Category
import com.dcns.dailycost.data.model.Expense
import com.dcns.dailycost.data.model.Income

/**
 *
 */
interface Transaction {

	val id: Int
	val userId: Int
	val name: String
	val amount: Double
	val payment: WalletType
	val date: Long
	val category: Category

	val isIncome: Boolean
		get() = this is Income

	val isExpense: Boolean
		get() = this is Expense

	/**
	 * Parse amount
	 *
	 * example:
	 *
	 * amount: 5000.0
	 *
	 * in indonesian: Rp 5.000,00
	 */
	fun parseAmount(countryCode: String): String {
		val formattedAmount = CurrencyFormatter.format(
			locale = primarySystemLocale,
			amount = amount,
			countryCode = countryCode
		)

		return if (isExpense) "- $formattedAmount" else formattedAmount
	}

}