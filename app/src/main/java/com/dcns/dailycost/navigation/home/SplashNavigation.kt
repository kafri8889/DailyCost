package com.dcns.dailycost.navigation.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.ui.splash.SplashScreen

fun NavGraphBuilder.SplashNavigation() {
    composable(TopLevelDestinations.splash.route) {
        SplashScreen()
    }
}
