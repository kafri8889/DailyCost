package com.dcns.dailycost.ui.transactions

import android.os.Parcelable
import com.dcns.dailycost.data.TransactionType
import com.dcns.dailycost.foundation.common.Transaction
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class TransactionsState(
	/**
	 * Kalo null, semua transaksi (income & expense)
	 */
	val selectedTransactionType: TransactionType? = null,
	val transactions: @RawValue List<Transaction> = emptyList(),
): Parcelable
