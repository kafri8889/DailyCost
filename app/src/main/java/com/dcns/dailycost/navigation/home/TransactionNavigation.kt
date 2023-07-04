package com.dcns.dailycost.navigation.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.ui.transaction.TransactionScreen
import com.dcns.dailycost.ui.transaction.TransactionViewModel

fun NavGraphBuilder.TransactionNavigation(navigationActions: NavigationActions) {
    composable(
        route = TopLevelDestinations.Home.expense.route
    ) { backEntry ->
        val mViewModel = hiltViewModel<TransactionViewModel>(backEntry)

        TransactionScreen(
            viewModel = mViewModel,
            navigationActions = navigationActions
        )
    }
}
