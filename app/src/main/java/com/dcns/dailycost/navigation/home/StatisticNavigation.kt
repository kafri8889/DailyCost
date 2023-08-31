package com.dcns.dailycost.navigation.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.ui.statistic.StatisticScreen
import com.dcns.dailycost.ui.statistic.StatisticViewModel

fun NavGraphBuilder.StatisticNavigation(navigationActions: NavigationActions) {
	composable(
		route = TopLevelDestinations.Home.statistic.route
	) { backEntry ->
		val mViewModel = hiltViewModel<StatisticViewModel>(backEntry)

		StatisticScreen(
			viewModel = mViewModel,
			navigationActions = navigationActions
		)
	}
}
