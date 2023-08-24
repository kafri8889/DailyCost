package com.dcns.dailycost.navigation.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.ui.transactions.TransactionsScreen
import com.dcns.dailycost.ui.transactions.TransactionsViewModel

fun NavGraphBuilder.TransactionsNavigation(navActions: NavigationActions) {
	composable(
		route = TopLevelDestinations.Home.transactions.route,
		arguments = TopLevelDestinations.Home.transactions.arguments
	) { backEntry ->
		val viewModel = hiltViewModel<TransactionsViewModel>(backEntry)

		TransactionsScreen(
			viewModel = viewModel,
			navigationActions = navActions
		)
	}
}
