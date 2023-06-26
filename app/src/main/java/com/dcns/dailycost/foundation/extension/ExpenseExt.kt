package com.dcns.dailycost.foundation.extension

import com.dcns.dailycost.data.model.Expense
import com.dcns.dailycost.data.model.local.ExpenseDb

fun Expense.toExpenseDb(): ExpenseDb {
    return ExpenseDb(
        id = id,
        userId = userId,
        name = name,
        amount = amount,
        payment = payment,
        date = date,
        categoryId = category.id,
    )
}
