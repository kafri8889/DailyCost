package com.dcns.dailycost.ui.app

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.theme.DailyCostTheme
import com.dcns.dailycost.ui.login_register.LoginRegisterScreen
import com.dcns.dailycost.ui.login_register.LoginRegisterViewModel
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterialApi::class)
@Composable
fun DailyCostApp(darkTheme: Boolean = false) {

    val density = LocalDensity.current

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
                    // TODO: Cek kalo user udah login, langsung ke rute `home`
                    startDestination = TopLevelDestinations.LoginRegister.ROOT_ROUTE
                ) {

                    // Nested navigasi untuk login atau register
                    navigation(
                        startDestination = TopLevelDestinations.LoginRegister.loginRegister.route,
                        route = TopLevelDestinations.LoginRegister.ROOT_ROUTE
                    ) {
                        composable(
                            route = TopLevelDestinations.LoginRegister.loginRegister.route
                        ) { backEntry ->
                            val viewModel = hiltViewModel<LoginRegisterViewModel>(backEntry)

                            LoginRegisterScreen(
                                viewModel = viewModel,
                                navigationActions = navActions
                            )
                        }
                    }
                }
            }
        }
    }

}
