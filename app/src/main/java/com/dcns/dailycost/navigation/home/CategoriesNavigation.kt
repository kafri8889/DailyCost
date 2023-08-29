package com.dcns.dailycost.navigation.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.ui.categories.CategoriesScreen
import com.dcns.dailycost.ui.categories.CategoriesViewModel

fun NavGraphBuilder.CategoriesNavigation(navActions: NavigationActions) {
	composable(
		route = TopLevelDestinations.Home.categories.route,
		arguments = TopLevelDestinations.Home.categories.arguments
	) { backEntry ->
		val viewModel = hiltViewModel<CategoriesViewModel>(backEntry)

		CategoriesScreen(
			viewModel = viewModel,
			navigationActions = navActions
		)
	}
}
