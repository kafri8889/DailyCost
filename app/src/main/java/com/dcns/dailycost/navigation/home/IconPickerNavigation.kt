package com.dcns.dailycost.navigation.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.foundation.base.sharedViewModel
import com.dcns.dailycost.navigation.home.shared.HomeSharedViewModel
import com.dcns.dailycost.ui.icon_picker.IconPickerScreen
import com.dcns.dailycost.ui.icon_picker.IconPickerViewModel

fun NavGraphBuilder.IconPickerNavigation(navigationActions: NavigationActions) {
	dialog(
		route = TopLevelDestinations.Home.iconPicker.route
	) { backEntry ->
		val homeSharedViewModel = backEntry.sharedViewModel<HomeSharedViewModel>(navigationActions.navController)
		val viewModel = hiltViewModel<IconPickerViewModel>(backEntry)

		IconPickerScreen(
			viewModel = viewModel,
			navigationActions = navigationActions,
			sharedViewModel = homeSharedViewModel
		)
	}
}
