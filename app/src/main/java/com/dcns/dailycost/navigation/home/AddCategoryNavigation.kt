package com.dcns.dailycost.navigation.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.ui.add_category.AddCategoryScreen
import com.dcns.dailycost.ui.add_category.AddCategoryViewModel

fun NavGraphBuilder.AddCategoryNavigation(navigationActions: NavigationActions) {
	composable(TopLevelDestinations.Home.addCategory.route) { backEntry ->
		val mViewModel = hiltViewModel<AddCategoryViewModel>(backEntry)

		AddCategoryScreen(
			viewModel = mViewModel,
			navigationActions = navigationActions
		)
	}
}
