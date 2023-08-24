package com.dcns.dailycost.ui.dashboard

import android.content.Context

sealed interface DashboardAction {

	data class Refresh(val context: Context): DashboardAction

}