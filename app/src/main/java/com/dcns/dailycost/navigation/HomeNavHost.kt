package com.dcns.dailycost.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.dcns.dailycost.data.TopLevelDestinations

fun NavGraphBuilder.HomeNavHost(
    builder: NavGraphBuilder.() -> Unit
) {
    navigation(
        startDestination = TopLevelDestinations.Home.dashboard.route,
        route = TopLevelDestinations.Home.ROOT_ROUTE,
        builder = builder
    )
}
