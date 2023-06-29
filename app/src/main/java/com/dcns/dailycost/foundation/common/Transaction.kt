package com.dcns.dailycost.foundation.common

import com.dcns.dailycost.data.model.Expense
import com.dcns.dailycost.data.model.Income

abstract class Transaction {

    val isIncome: Boolean
        get() = this is Income

    val isExpense: Boolean
        get() = this is Expense

}