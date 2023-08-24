package com.dcns.dailycost.navigation.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.ui.change_language.ChangeLanguageScreen
import com.dcns.dailycost.ui.change_language.ChangeLanguageViewModel
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.ChangeLanguageNavigation(navigationActions: NavigationActions) {
	bottomSheet(TopLevelDestinations.Home.changeLanguage.route) { backEntry ->
		val mViewModel = hiltViewModel<ChangeLanguageViewModel>(backEntry)

		ChangeLanguageScreen(
			viewModel = mViewModel,
			navigationActions = navigationActions
		)
	}
}
