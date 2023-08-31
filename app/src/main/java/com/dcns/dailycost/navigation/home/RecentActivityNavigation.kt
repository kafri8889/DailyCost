package com.dcns.dailycost.navigation.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.ui.recent_activity.RecentActivityScreen
import com.dcns.dailycost.ui.recent_activity.RecentActivityViewModel

fun NavGraphBuilder.RecentActivityNavigation(navigationActions: NavigationActions) {
	composable(
		route = TopLevelDestinations.Home.recentActivity.route
	) { backEntry ->
		val mViewModel = hiltViewModel<RecentActivityViewModel>(backEntry)

		RecentActivityScreen(
			viewModel = mViewModel,
			navigationActions = navigationActions
		)
	}
}
