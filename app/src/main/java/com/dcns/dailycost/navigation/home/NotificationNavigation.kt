package com.dcns.dailycost.navigation.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.ui.notification.NotificationScreen
import com.dcns.dailycost.ui.notification.NotificationViewModel

fun NavGraphBuilder.NotificationNavigation(navigationActions: NavigationActions) {
	composable(
		route = TopLevelDestinations.Home.notification.route,
		deepLinks = TopLevelDestinations.Home.notification.deepLinks
	) { backEntry ->
		val mViewModel = hiltViewModel<NotificationViewModel>(backEntry)

		NotificationScreen(
			viewModel = mViewModel,
			navigationActions = navigationActions
		)
	}
}
