package com.dcns.dailycost.ui.transaction

import com.dcns.dailycost.data.TransactionType
import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.model.Category

sealed interface TransactionAction {
	data class SetTransactionType(val type: TransactionType): TransactionAction
	data class SetCategory(val category: Category): TransactionAction
	data class SetName(val name: String): TransactionAction
	data class SetPayment(val payment: WalletType): TransactionAction
	data class SetAmount(val amount: Double): TransactionAction
	data class SetDate(val date: Long): TransactionAction

	object Delete: TransactionAction
	object Save: TransactionAction
}