package com.dcns.dailycost.navigation.login_register

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.ui.login_register.LoginRegisterScreen
import com.dcns.dailycost.ui.login_register.LoginRegisterViewModel

fun NavGraphBuilder.LoginRegisterNavigation(navigationActions: NavigationActions) {
    composable(
        route = TopLevelDestinations.LoginRegister.loginRegister.route
    ) { backEntry ->
        val mViewModel =
            hiltViewModel<LoginRegisterViewModel>(backEntry)

        LoginRegisterScreen(
            viewModel = mViewModel,
            navigationActions = navigationActions
        )
    }
}
