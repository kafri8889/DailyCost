package com.dcns.dailycost.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class TransactionType: Parcelable {
	Income,
	Expense;

	val isIncome: Boolean
		get() = this == Income

	val isExpense: Boolean
		get() = this == Expense
}