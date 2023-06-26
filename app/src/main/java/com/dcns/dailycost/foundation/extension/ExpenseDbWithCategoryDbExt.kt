package com.dcns.dailycost.foundation.extension

import com.dcns.dailycost.data.model.Expense
import com.dcns.dailycost.data.model.local.relation.ExpenseDbWithCategoryDb

fun ExpenseDbWithCategoryDb.toExpense(): Expense {
    return Expense(
        id = expenseDb.id,
        userId = expenseDb.userId,
        name = expenseDb.name,
        amount = expenseDb.amount,
        payment = expenseDb.payment,
        date = expenseDb.date,
        category = categoryDb.toCategory()
    )
}
