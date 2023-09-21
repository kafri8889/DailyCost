package com.dcns.dailycost.navigation.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.foundation.base.sharedViewModel
import com.dcns.dailycost.navigation.home.shared.HomeSharedViewModel
import com.dcns.dailycost.ui.category.CategoryScreen
import com.dcns.dailycost.ui.category.CategoryViewModel

fun NavGraphBuilder.CategoryNavigation(navigationActions: NavigationActions) {
	composable(
		route = TopLevelDestinations.Home.category.route,
		arguments = TopLevelDestinations.Home.category.arguments
	) { backEntry ->
		val homeSharedViewModel = backEntry.sharedViewModel<HomeSharedViewModel>(navigationActions.navController)
		val mViewModel = hiltViewModel<CategoryViewModel>(backEntry)

		CategoryScreen(
			viewModel = mViewModel,
			navigationActions = navigationActions,
			sharedViewModel = homeSharedViewModel
		)
	}
}
