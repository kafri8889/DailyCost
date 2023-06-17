package com.dcns.dailycost.ui.app

sealed interface DailyCostAppAction {

    data class UpdateCurrentDestinationRoute(val route: String): DailyCostAppAction

}