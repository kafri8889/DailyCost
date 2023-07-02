package com.dcns.dailycost.ui.dashboard

import com.dcns.dailycost.ProtoUserBalance
import com.dcns.dailycost.ProtoUserCredential
import com.dcns.dailycost.data.model.Expense
import com.dcns.dailycost.data.model.Income
import com.dcns.dailycost.data.model.UserBalance
import com.dcns.dailycost.data.model.UserCredential
import com.dcns.dailycost.foundation.extension.toUserBalance
import com.dcns.dailycost.foundation.extension.toUserCredential

data class DashboardState(
    val credential: UserCredential = ProtoUserCredential().toUserCredential(),
    val balance: UserBalance = ProtoUserBalance().toUserBalance(),
    val internetConnectionAvailable: Boolean = true,
    val isRefreshing: Boolean = false,
    val expenses: List<Expense> = emptyList(),
    val incomes: List<Income> = emptyList(),
)
