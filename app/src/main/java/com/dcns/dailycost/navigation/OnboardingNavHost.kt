package com.dcns.dailycost.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.dcns.dailycost.data.TopLevelDestinations

fun NavGraphBuilder.OnboardingNavHost(
	builder: NavGraphBuilder.() -> Unit
) {
	navigation(
		startDestination = TopLevelDestinations.Onboarding.onboarding.route,
		route = TopLevelDestinations.Onboarding.ROOT_ROUTE,
		builder = builder
	)
}
