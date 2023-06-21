package com.dcns.dailycost.navigation.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.ui.setting.SettingScreen
import com.dcns.dailycost.ui.setting.SettingViewModel

fun NavGraphBuilder.SettingNavigation(
    navigationActions: NavigationActions,
    onNavigationIconClicked: () -> Unit
) {
    composable(
        route = TopLevelDestinations.Home.setting.route
    ) { backEntry ->
        val mViewModel = hiltViewModel<SettingViewModel>(backEntry)

        SettingScreen(
            viewModel = mViewModel,
            navigationActions = navigationActions,
            onNavigationIconClicked = onNavigationIconClicked
        )
    }
}
