package com.dcns.dailycost.foundation.extension

import com.dcns.dailycost.data.model.Income
import com.dcns.dailycost.data.model.local.relation.IncomeDbWithCategoryDb

fun IncomeDbWithCategoryDb.toIncome(): Income {
    return Income(
        id = incomeDb.id,
        userId = incomeDb.userId,
        name = incomeDb.name,
        amount = incomeDb.amount,
        payment = incomeDb.payment,
        date = incomeDb.date,
        category = categoryDb.toCategory()
    )
}
