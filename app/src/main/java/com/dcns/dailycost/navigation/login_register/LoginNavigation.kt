package com.dcns.dailycost.navigation.login_register

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.ui.login.LoginScreen
import com.dcns.dailycost.ui.login.LoginViewModel

fun NavGraphBuilder.LoginNavigation(navigationActions: NavigationActions) {
	composable(
		route = TopLevelDestinations.LoginRegister.login.route,
		deepLinks = TopLevelDestinations.LoginRegister.login.deepLinks
	) { backEntry ->
		val mViewModel = hiltViewModel<LoginViewModel>(backEntry)

		LoginScreen(
			viewModel = mViewModel,
			navigationActions = navigationActions
		)
	}
}
