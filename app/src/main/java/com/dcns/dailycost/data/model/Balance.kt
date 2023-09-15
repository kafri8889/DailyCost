package com.dcns.dailycost.data.model

import android.os.Parcelable
import com.dcns.dailycost.data.WalletType
import kotlinx.parcelize.Parcelize

/**
 * Class ini berisi
 * - wallet type
 * - saldo
 * - pengeluaran bulanan
 */
@Parcelize
data class Balance(
	val amount: Double,
	val walletType: WalletType,
	/**
	 * Pengeluaran bulan ini
	 */
	val monthlyExpense: Double,
): Parcelable
