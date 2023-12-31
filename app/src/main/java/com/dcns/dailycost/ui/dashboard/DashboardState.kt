package com.dcns.dailycost.ui.dashboard

import android.os.Parcelable
import com.dcns.dailycost.ProtoUserCredential
import com.dcns.dailycost.data.model.Balance
import com.dcns.dailycost.data.model.Expense
import com.dcns.dailycost.data.model.Income
import com.dcns.dailycost.data.model.UserCredential
import com.dcns.dailycost.foundation.extension.toUserCredential
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class DashboardState(
	val credential: UserCredential = ProtoUserCredential().toUserCredential(),
	val balances: List<Balance> = emptyList(),
	val unreadNotificationCount: Int = 0,
	val internetConnectionAvailable: Boolean = true,
	val initialBalanceVisibility: Boolean = false,
	val isRefreshing: Boolean = false,
	/**
	 * - [Income]
	 * - [Expense]
	 * - [Note]
	 */
	val recentActivity: @RawValue List<Any> = emptyList(),
	val expenses: List<Expense> = emptyList(),
	val incomes: List<Income> = emptyList(),
): Parcelable
