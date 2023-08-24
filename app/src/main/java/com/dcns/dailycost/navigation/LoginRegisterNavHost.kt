package com.dcns.dailycost.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.dcns.dailycost.data.TopLevelDestinations

fun NavGraphBuilder.LoginRegisterNavHost(
	builder: NavGraphBuilder.() -> Unit
) {
	navigation(
		startDestination = TopLevelDestinations.LoginRegister.login.route,
		route = TopLevelDestinations.LoginRegister.ROOT_ROUTE,
		builder = builder
	)
}
