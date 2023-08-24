package com.dcns.dailycost.navigation.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.ui.dashboard.DashboardScreen
import com.dcns.dailycost.ui.dashboard.DashboardViewModel

fun NavGraphBuilder.DashboardNavigation(
	navigationActions: NavigationActions,
	onNavigationIconClicked: () -> Unit
) {
	composable(
		route = TopLevelDestinations.Home.dashboard.route
	) { backEntry ->
		val mViewModel = hiltViewModel<DashboardViewModel>(backEntry)

		DashboardScreen(
			viewModel = mViewModel,
			navigationActions = navigationActions,
			onNavigationIconClicked = onNavigationIconClicked
		)
	}
}
