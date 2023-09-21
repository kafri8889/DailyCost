package com.dcns.dailycost.foundation.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

/**
 * Shared view model, digunakan untuk berbagi state dalam scope navigation graph ([androidx.navigation.compose.navigation])
 */

/**
 * Digunakan untuk mendapatkan instance dari [VM]
 */
@Composable
inline fun <reified VM: BaseViewModel<*, *>> NavBackStackEntry.sharedViewModel(
	navController: NavHostController
): VM {
	val navGraphRoute = destination.parent?.route ?: return viewModel()
	val parentEntry = remember(this) {
		navController.getBackStackEntry(navGraphRoute)
	}

	return viewModel(parentEntry)
}
