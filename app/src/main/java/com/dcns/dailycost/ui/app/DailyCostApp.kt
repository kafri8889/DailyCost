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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dcns.dailycost.R
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestination
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.data.drawerDestinations
import com.dcns.dailycost.foundation.common.DailyCostBiometricManager
import com.dcns.dailycost.navigation.HomeNavHost
import com.dcns.dailycost.navigation.LoginRegisterNavHost
import com.dcns.dailycost.navigation.OnboardingNavHost
import com.dcns.dailycost.navigation.home.ChangeLanguageNavigation
import com.dcns.dailycost.navigation.home.CreateEditNoteNavigation
import com.dcns.dailycost.navigation.home.DashboardNavigation
import com.dcns.dailycost.navigation.home.NoteNavigation
import com.dcns.dailycost.navigation.home.SettingNavigation
import com.dcns.dailycost.navigation.home.SplashNavigation
import com.dcns.dailycost.navigation.login_register.LoginNavigation
import com.dcns.dailycost.navigation.onboarding.OnboardingNavigation
import com.dcns.dailycost.theme.DailyCostTheme
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterialApi::class)
@Composable
fun DailyCostApp(
    darkTheme: Boolean = false,
    viewModel: DailyCostAppViewModel = hiltViewModel(),
    biometricManager: DailyCostBiometricManager
) {

    val context = LocalContext.current
    val density = LocalDensity.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
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

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(navBackStackEntry) {
        navBackStackEntry?.destination?.route?.let { route ->
            viewModel.onAction(DailyCostAppAction.UpdateCurrentDestinationRoute(route))
        }
    }

    LaunchedEffect(state.isSecureAppEnabled, state.isBiometricAuthenticated) {
        val canAuth = DailyCostBiometricManager.canAuthenticateWithAuthenticators(context)

        if (state.isSecureAppEnabled && !state.isBiometricAuthenticated && canAuth) {
            biometricManager.showAuthentication(
                title = context.getString(R.string.authentication),
                subtitle = "",
                description = "",
                negativeButtonText = context.getString(R.string.cancel)
            )
        } else viewModel.onAction(DailyCostAppAction.IsBiometricAuthenticated(true))
    }

    LaunchedEffect(state.userCredential, state.isFirstInstall, state.userFirstEnteredApp) {
        if (state.userFirstEnteredApp && state.isFirstInstall != null && state.userCredential != null) {
            navActions.navigateTo(
                inclusivePopUpTo = true,
                destination = when {
                    state.isFirstInstall == true -> TopLevelDestinations.Onboarding.onboarding
                    state.userCredential!!.isLoggedIn -> TopLevelDestinations.Home.dashboard
                    else -> TopLevelDestinations.LoginRegister.login
                }
            )

            viewModel.onAction(DailyCostAppAction.UpdateUserFirstEnteredApp(false))
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is DailyCostAppUiEvent.LanguageChanged -> {
                    if (state.userCredential?.isLoggedIn == true) {
                        navActions.navigateTo(TopLevelDestinations.Home.dashboard)
                    }
                }
                is DailyCostAppUiEvent.NavigateTo -> {
                    navActions.navigateTo(event.dest)
                }
            }
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

            val drawerState = rememberDrawerState(
                initialValue = DrawerValue.Closed
            )

            val onNavigationIconClicked: () -> Unit = {
                scope.launch {
                    if (drawerState.isOpen) drawerState.close()
                    else drawerState.open()
                }
            }

            BackHandler(drawerState.isOpen) {
                scope.launch {
                    drawerState.close()
                }
            }

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
                    DailyCostNavHost(
                        navController = navController,
                        navActions = navActions,
                        onNavigationIconClicked = onNavigationIconClicked
                    )
                }
            }
        }
    }

}

@Composable
private fun DailyCostNavHost(
    navController: NavHostController,
    navActions: NavigationActions,
    onNavigationIconClicked: () -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = TopLevelDestinations.splash.route
    ) {
        SplashNavigation()

        // Nested navigasi untuk onboarding
        OnboardingNavHost {
            OnboardingNavigation(navActions)
        }

        // Nested navigasi untuk login atau register
        LoginRegisterNavHost {
            LoginNavigation(navActions)
        }

        // Nested navigasi untuk dashboard (ketika user berhasil login)
        HomeNavHost {
            ChangeLanguageNavigation(navActions)
            CreateEditNoteNavigation(navActions)

            DashboardNavigation(
                navigationActions = navActions,
                onNavigationIconClicked = onNavigationIconClicked
            )

            NoteNavigation(
                navigationActions = navActions,
                onNavigationIconClicked = onNavigationIconClicked
            )

            SettingNavigation(
                navigationActions = navActions,
                onNavigationIconClicked = onNavigationIconClicked
            )
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
            items(
                items = drawerDestinations,
                key = { dest -> dest.route }
            ) { destination ->
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
