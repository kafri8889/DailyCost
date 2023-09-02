package com.dcns.dailycost.ui.app

sealed interface DailyCostAppAction {

	data class UpdateCurrentDestinationRoute(val route: String): DailyCostAppAction

	data class IsBiometricAuthenticated(val authenticated: Boolean): DailyCostAppAction

	data class UpdateUserFirstEnteredApp(val first: Boolean): DailyCostAppAction

	/**
	 * Digunakan untuk menentukan apakah navController diizinkan untuk menavigasi (lihat dibagian "navigasi otomatis")
	 *
	 * Tujuan action ini dibuat adalah untuk mencegah navController menavigasi saat user masuk melalui deeplink
	 */
	data class CanNavigate(val can: Boolean): DailyCostAppAction

}