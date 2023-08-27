package com.dcns.dailycost.ui.transaction

import com.dcns.dailycost.data.ActionMode
import com.dcns.dailycost.data.TransactionType
import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.datasource.local.LocalCategoryDataProvider
import com.dcns.dailycost.data.model.Category

data class TransactionState(
	// Transaction part
	val id: Int = -1,
	val title: String = "",
	val amount: Double = 0.0,
	val payment: WalletType = WalletType.Cash,
	val date: Long = System.currentTimeMillis(),
	val category: Category = LocalCategoryDataProvider.other,

	/**
	 * Available category in local db
	 */
	val availableCategory: List<Category> = emptyList(),
	val actionMode: ActionMode = ActionMode.New,
	val transactionType: TransactionType = TransactionType.Income,

	val titleError: Boolean = false
)
