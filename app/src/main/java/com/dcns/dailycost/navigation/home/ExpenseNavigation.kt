package com.dcns.dailycost.navigation.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.ui.expense.ExpenseScreen
import com.dcns.dailycost.ui.expense.ExpenseViewModel

fun NavGraphBuilder.ExpenseNavigation(navigationActions: NavigationActions) {
    composable(
        route = TopLevelDestinations.Home.expense.route
    ) { backEntry ->
        val mViewModel = hiltViewModel<ExpenseViewModel>(backEntry)

        ExpenseScreen(
            viewModel = mViewModel,
            navigationActions = navigationActions
        )
    }
}
