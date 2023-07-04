package com.dcns.dailycost.foundation.extension

import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.model.Category
import com.dcns.dailycost.data.model.Income
import com.dcns.dailycost.data.model.remote.IncomeResponseData

inline fun IncomeResponseData.toIncome(
    date: (date: String) -> Long,
    category: (categoryName: String) -> Category
): Income {
    return Income(
        id = incomeId,
        userId = userId,
        name = name,
        amount = amount.toDouble(),
        payment = WalletType.parse(payment),
        date = date(this.date),
        category = category(this.category),
    )
}
