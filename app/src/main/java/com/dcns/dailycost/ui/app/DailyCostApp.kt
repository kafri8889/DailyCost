package com.dcns.dailycost.ui.app

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.theme.DailyCostTheme
import com.dcns.dailycost.ui.dashboard.DashboardScreen
import com.dcns.dailycost.ui.dashboard.DashboardViewModel
import com.dcns.dailycost.ui.login_register.LoginRegisterScreen
import com.dcns.dailycost.ui.login_register.LoginRegisterViewModel
import com.dcns.dailycost.ui.splash.SplashScreen
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterialApi::class)
@Composable
fun DailyCostApp(
    darkTheme: Boolean = false,
    viewModel: DailyCostAppViewModel = hiltViewModel()
) {

    val density = LocalDensity.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    val systemUiController = rememberSystemUiController()

    val bottomSheetNavigator = remember {
        BottomSheetNavigator(
            ModalBottomSheetState(
                density = density,
                isSkipHalfExpanded = true,
                initialValue = ModalBottomSheetValue.Hidden
            )
        )
    }

    val navController = rememberNavController(bottomSheetNavigator)

    val navActions = remember(navController) {
        NavigationActions(navController)
    }

    LaunchedEffect(state.userCredential) {
        state.userCredential?.let {
            navActions.navigateTo(
                if (it.isLoggedIn) {
                    TopLevelDestinations.Home.dashboard
                } else TopLevelDestinations.LoginRegister.loginRegister
            )
        }
    }

    DailyCostTheme(darkTheme) {

        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = !darkTheme
            )
        }

        Surface(color = MaterialTheme.colorScheme.background) {
            ModalBottomSheetLayout(
                bottomSheetNavigator = bottomSheetNavigator,
                sheetShape = MaterialTheme.shapes.large,
                sheetBackgroundColor = MaterialTheme.colorScheme.surface,
                sheetContentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.surface),
                scrimColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.64f)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = TopLevelDestinations.splash.route
                ) {

                    // Nested navigasi untuk login atau register
                    navigation(
                        startDestination = TopLevelDestinations.LoginRegister.loginRegister.route,
                        route = TopLevelDestinations.LoginRegister.ROOT_ROUTE
                    ) {
                        composable(
                            route = TopLevelDestinations.LoginRegister.loginRegister.route
                        ) { backEntry ->
                            val mViewModel = hiltViewModel<LoginRegisterViewModel>(backEntry)

                            LoginRegisterScreen(
                                viewModel = mViewModel,
                                navigationActions = navActions
                            )
                        }
                    }

                    // Nested navigasi untuk dashboard (ketika user berhasil login)
                    navigation(
                        startDestination = TopLevelDestinations.Home.dashboard.route,
                        route = TopLevelDestinations.Home.ROOT_ROUTE
                    ) {
                        composable(
                            route = TopLevelDestinations.Home.dashboard.route
                        ) { backEntry ->
                            val mViewModel = hiltViewModel<DashboardViewModel>(backEntry)

                            DashboardScreen(
                                viewModel = mViewModel,
                                navigationActions = navActions
                            )
                        }
                    }

                    composable(TopLevelDestinations.splash.route) {
                        SplashScreen()
                    }
                }
            }
        }
    }

}
