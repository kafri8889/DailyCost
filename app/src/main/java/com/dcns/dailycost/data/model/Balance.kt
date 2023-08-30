package com.dcns.dailycost.data.model

import com.dcns.dailycost.data.WalletType

/**
 * Class ini berisi
 * - wallet type
 * - saldo
 * - pengeluaran bulanan
 */
data class Balance(
	val amount: Double,
	val walletType: WalletType,
	/**
	 * Pengeluaran bulan ini
	 */
	val monthlyExpense: Double,
)
