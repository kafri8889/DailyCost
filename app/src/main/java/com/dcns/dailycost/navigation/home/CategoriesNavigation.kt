package com.dcns.dailycost.navigation.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.foundation.base.sharedViewModel
import com.dcns.dailycost.navigation.home.shared.HomeSharedViewModel
import com.dcns.dailycost.ui.categories.CategoriesScreen
import com.dcns.dailycost.ui.categories.CategoriesViewModel

fun NavGraphBuilder.CategoriesNavigation(navigationActions: NavigationActions) {
	composable(
		route = TopLevelDestinations.Home.categories.route,
		arguments = TopLevelDestinations.Home.categories.arguments
	) { backEntry ->
		val homeSharedViewModel = backEntry.sharedViewModel<HomeSharedViewModel>(navigationActions.navController)
		val viewModel = hiltViewModel<CategoriesViewModel>(backEntry)

		CategoriesScreen(
			viewModel = viewModel,
			navigationActions = navigationActions,
			sharedViewModel = homeSharedViewModel
		)
	}
}
