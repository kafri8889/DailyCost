package com.dcns.dailycost.navigation.login_register

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.ui.register.RegisterScreen
import com.dcns.dailycost.ui.register.RegisterViewModel

fun NavGraphBuilder.RegisterNavigation(navigationActions: NavigationActions) {
    composable(
        route = TopLevelDestinations.LoginRegister.register.route
    ) { backEntry ->
        val mViewModel = hiltViewModel<RegisterViewModel>(backEntry)

        RegisterScreen(
            viewModel = mViewModel,
            navigationActions = navigationActions
        )
    }
}
