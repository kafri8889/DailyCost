package com.dcns.dailycost.navigation.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.foundation.base.sharedViewModel
import com.dcns.dailycost.navigation.home.shared.HomeSharedViewModel
import com.dcns.dailycost.ui.transaction.TransactionScreen
import com.dcns.dailycost.ui.transaction.TransactionViewModel

fun NavGraphBuilder.TransactionNavigation(navigationActions: NavigationActions) {
	composable(
		route = TopLevelDestinations.Home.transaction.route,
		arguments = TopLevelDestinations.Home.transaction.arguments
	) { backEntry ->
		val homeSharedViewModel = backEntry.sharedViewModel<HomeSharedViewModel>(navigationActions.navController)
		val mViewModel = hiltViewModel<TransactionViewModel>(backEntry)

		TransactionScreen(
			viewModel = mViewModel,
			navigationActions = navigationActions,
			sharedViewModel = homeSharedViewModel
		)
	}
}
