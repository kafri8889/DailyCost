package com.dcns.dailycost.foundation.extension

import com.dcns.dailycost.data.model.Income
import com.dcns.dailycost.data.model.local.IncomeDb

fun Income.toIncomeDb(): IncomeDb {
    return IncomeDb(
        id = id,
        userId = userId,
        name = name,
        amount = amount,
        payment = payment,
        date = date,
        categoryId = category.id,
    )
}
