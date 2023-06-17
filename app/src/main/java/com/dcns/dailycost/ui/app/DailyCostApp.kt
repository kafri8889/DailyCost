package com.dcns.dailycost.ui.app

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.dcns.dailycost.MainActivity
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestination
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.data.drawerDestinations
import com.dcns.dailycost.foundation.common.LocalDrawerState
import com.dcns.dailycost.theme.DailyCostTheme
import com.dcns.dailycost.ui.dashboard.DashboardScreen
import com.dcns.dailycost.ui.dashboard.DashboardViewModel
import com.dcns.dailycost.ui.login_register.LoginRegisterScreen
import com.dcns.dailycost.ui.login_register.LoginRegisterViewModel
import com.dcns.dailycost.ui.setting.SettingScreen
import com.dcns.dailycost.ui.setting.SettingViewModel
import com.dcns.dailycost.ui.splash.SplashScreen
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterialApi::class)
@Composable
fun DailyCostApp(
    darkTheme: Boolean = false,
    viewModel: DailyCostAppViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val density = LocalDensity.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val systemUiController = rememberSystemUiController()
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

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

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(navBackStackEntry) {
        navBackStackEntry?.destination?.route?.let { route ->
            viewModel.onAction(DailyCostAppAction.UpdateCurrentDestinationRoute(route))
        }
    }

    LaunchedEffect(state.userCredential) {
        state.userCredential?.let {
            navActions.navigateTo(
                inclusivePopUpTo = true,
                destination = if (it.isLoggedIn) {
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
            DailyCostDrawer(
                state = drawerState,
                selectedDestinationRoute = state.currentDestinationRoute,
                onDestinationClicked = { destination ->
                    navActions.navigateTo(destination)
                    scope.launch {
                        drawerState.close()
                    }
                }
            ) {
                DailyCostBottomSheetLayout(bottomSheetNavigator) {
                    CompositionLocalProvider(
                        LocalDrawerState provides drawerState
                    ) {
                        BackHandler {
                            when {
                                drawerState.isOpen -> {
                                    scope.launch {
                                        drawerState.close()
                                    }
                                }
                                else -> {
                                    // TODO: fix multiple back stack
                                    val previousDestination = navController.previousBackStackEntry?.destination?.id

                                    if (previousDestination != null) {
                                        navActions.popBackStack(previousDestination)
                                    } else {
                                        (context as MainActivity).finishAndRemoveTask()
                                    }
                                }
                            }
                        }

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
                                    val mViewModel =
                                        hiltViewModel<LoginRegisterViewModel>(backEntry)

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

                                composable(
                                    route = TopLevelDestinations.Home.setting.route
                                ) { backEntry ->
                                    val mViewModel = hiltViewModel<SettingViewModel>(backEntry)

                                    SettingScreen(
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
    }

}

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
private fun DailyCostBottomSheetLayout(
    navigator: BottomSheetNavigator,
    content: @Composable () -> Unit
) {
    ModalBottomSheetLayout(
        bottomSheetNavigator = navigator,
        sheetShape = MaterialTheme.shapes.large,
        sheetBackgroundColor = MaterialTheme.colorScheme.surface,
        sheetContentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.surface),
        scrimColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.64f),
        content = content
    )
}

@Composable
private fun DailyCostDrawer(
    state: DrawerState,
    selectedDestinationRoute: String,
    onDestinationClicked: (TopLevelDestination) -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        content = content,
        drawerState = state,
        gesturesEnabled = state.isOpen,
        drawerContent = {
            DailyCostDrawerContent(
                selectedDestinationRoute = selectedDestinationRoute,
                onDestinationClicked = onDestinationClicked
            )
        }
    )
}

@Composable
private fun DailyCostDrawerContent(
    selectedDestinationRoute: String,
    onDestinationClicked: (TopLevelDestination) -> Unit
) {
    ModalDrawerSheet {
        LazyColumn {
            items(drawerDestinations) { destination ->
                NavigationDrawerItem(
                    selected = selectedDestinationRoute == destination.route,
                    label = {
                        Text(stringResource(id = destination.name!!))
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = destination.icon!!),
                            contentDescription = null
                        )
                    },
                    onClick = {
                        onDestinationClicked(destination)
                    },
                    modifier = Modifier
                        .padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    }
}
