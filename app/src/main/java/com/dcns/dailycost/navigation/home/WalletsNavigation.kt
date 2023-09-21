package com.dcns.dailycost.navigation.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.foundation.base.sharedViewModel
import com.dcns.dailycost.navigation.home.shared.HomeSharedViewModel
import com.dcns.dailycost.ui.wallets.WalletsScreen
import com.dcns.dailycost.ui.wallets.WalletsViewModel

fun NavGraphBuilder.WalletsNavigation(navigationActions: NavigationActions) {
	composable(
		route = TopLevelDestinations.Home.wallets.route,
		arguments = TopLevelDestinations.Home.wallets.arguments
	) { backEntry ->
		val homeSharedViewModel = backEntry.sharedViewModel<HomeSharedViewModel>(navigationActions.navController)
		val viewModel = hiltViewModel<WalletsViewModel>(backEntry)

		WalletsScreen(
			viewModel = viewModel,
			navigationActions = navigationActions,
			homeSharedViewModel = homeSharedViewModel
		)
	}
}
