package com.dcns.dailycost.navigation.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.ui.top_up.TopUpScreen
import com.dcns.dailycost.ui.top_up.TopUpViewModel
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.TopUpNavigation(navigationActions: NavigationActions) {
    bottomSheet(TopLevelDestinations.Home.topUp.route) { backEntry ->
        val mViewModel = hiltViewModel<TopUpViewModel>(backEntry)

        TopUpScreen(
            viewModel = mViewModel,
            navigationActions = navigationActions
        )
    }
}
