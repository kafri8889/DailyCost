package com.dcns.dailycost.ui.dashboard

sealed interface DashboardAction {

    object Refresh: DashboardAction

}