package com.dcns.dailycost.foundation.extension

import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.model.Category
import com.dcns.dailycost.data.model.Expense
import com.dcns.dailycost.data.model.remote.GetExpenseResponseResult

inline fun GetExpenseResponseResult.toExpense(
    userId: Int,
    date: (date: String) -> Long,
    category: (categoryName: String) -> Category
): Expense {
    return Expense(
        id = id,
        userId = userId,
        name = name,
        amount = amount.toDouble(),
        payment = WalletType.parse(payment),
        date = date(this.date),
        category = category(this.category),
    )
}