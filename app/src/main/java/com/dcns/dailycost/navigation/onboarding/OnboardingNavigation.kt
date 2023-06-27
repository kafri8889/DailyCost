package com.dcns.dailycost.navigation.onboarding

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.ui.onboarding.OnboardingScreen
import com.dcns.dailycost.ui.onboarding.OnboardingViewModel

fun NavGraphBuilder.OnboardingNavigation(navigationActions: NavigationActions) {
    composable(
        route = TopLevelDestinations.Onboarding.onboarding.route
    ) { backEntry ->
        val mViewModel =
            hiltViewModel<OnboardingViewModel>(backEntry)

        OnboardingScreen(
            viewModel = mViewModel,
            navigationActions = navigationActions
        )
    }
}
