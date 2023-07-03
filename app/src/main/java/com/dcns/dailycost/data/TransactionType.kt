package com.dcns.dailycost.data

enum class TransactionType {
    Income,
    Expense;

    val isIncome: Boolean
        get() = this == Income

    val isExpense: Boolean
        get() = this == Expense
}