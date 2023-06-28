package com.dcns.dailycost.ui.app

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dcns.dailycost.MainActivity
import com.dcns.dailycost.R
import com.dcns.dailycost.data.Language
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.foundation.common.DailyCostBiometricManager
import com.dcns.dailycost.foundation.uicomponent.NoRippleIconButton
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
import com.dcns.dailycost.navigation.login_register.RegisterNavigation
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

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val closeDrawer: () -> Unit = {
        scope.launch {
            drawerState.close()
        }
    }

    val onNavigationIconClicked: () -> Unit = {
        scope.launch {
            if (drawerState.isOpen) drawerState.close()
            else drawerState.open()
        }
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
            DailyCostDrawer(
                state = drawerState,
                email = state.userCredential?.email ?: "",
                language = state.language,
                onNavigationIconClicked = onNavigationIconClicked,
                onCategoriesClicked = {
                    // TODO: ke categories screen
                    closeDrawer()
                },
                onSettingClicked = {
                    navActions.navigateTo(TopLevelDestinations.Home.setting)
                    closeDrawer()
                },
                onLanguageClicked = {
                    navActions.navigateTo(TopLevelDestinations.Home.changeLanguage)
                    closeDrawer()
                },
                onSignOutClicked = {
                    navActions.navigateTo(TopLevelDestinations.LoginRegister.login)
                    closeDrawer()
                }
            ) {
                DailyCostBottomSheetLayout(bottomSheetNavigator) {
                    BackHandler {
                        when {
                            drawerState.isOpen -> {
                                scope.launch {
                                    drawerState.close()
                                }
                            }
                            state.currentDestinationRoute == TopLevelDestinations.Home.dashboard.route -> {
                                (context as MainActivity).finishAndRemoveTask()
                            }
                            state.currentDestinationRoute == TopLevelDestinations.Home.changeLanguage.route -> {
                                navActions.popBackStack()
                            }
                            state.currentDestinationRoute == TopLevelDestinations.Home.setting.route -> {
                                navActions.navigateTo(
                                    destination = TopLevelDestinations.Home.dashboard,
                                    inclusivePopUpTo = true
                                )
                            }
                        }
                    }

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
            RegisterNavigation(navActions)
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
    email: String,
    language: Language,
    onNavigationIconClicked: () -> Unit,
    onCategoriesClicked: () -> Unit,
    onLanguageClicked: () -> Unit,
    onSignOutClicked: () -> Unit,
    onSettingClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        content = content,
        drawerState = state,
        gesturesEnabled = state.isOpen,
        drawerContent = {
            DailyCostDrawerContent(
                email = email,
                language = language,
                onCategoriesClicked = onCategoriesClicked,
                onNavigationIconClicked = onNavigationIconClicked,
                onSettingClicked = onSettingClicked,
                onLanguageClicked = onLanguageClicked,
                onSignOutClicked = onSignOutClicked
            )
        }
    )
}

@Composable
private fun DailyCostDrawerContent(
    email: String,
    language: Language,
    onNavigationIconClicked: () -> Unit,
    onCategoriesClicked: () -> Unit,
    onLanguageClicked: () -> Unit,
    onSignOutClicked: () -> Unit,
    onSettingClicked: () -> Unit
) {
    val signOutInteractionSource = remember { MutableInteractionSource() }

    ModalDrawerSheet {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .weight(1f)
        ) {
            item {
                ListItem(
                    headlineContent = {
                        Text(stringResource(id = R.string.dashboard))
                    },
                    leadingContent = {
                        IconButton(onClick = onNavigationIconClicked) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_left),
                                contentDescription = null
                            )
                        }
                    }
                )
            }

            item {
                val interactionSource = remember { MutableInteractionSource() }

                Box(
                    modifier = Modifier
                        .clickable(
                            indication = rememberRipple(),
                            interactionSource = interactionSource,
                            onClick = onCategoriesClicked
                        )
                ) {
                    ListItem(
                        headlineContent = {
                            Text(stringResource(id = R.string.categories))
                        },
                        supportingContent = {
                            Text(stringResource(id = R.string.manage_categories_change_icon_color))
                        },
                        leadingContent = {
                            NoRippleIconButton(
                                onClick = onCategoriesClicked,
                                interactionSource = interactionSource
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_pie_chart),
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }
            }

            item {
                val interactionSource = remember { MutableInteractionSource() }

                Box(
                    modifier = Modifier
                        .clickable(
                            indication = rememberRipple(),
                            interactionSource = interactionSource,
                            onClick = onSettingClicked
                        )
                ) {
                    ListItem(
                        headlineContent = {
                            Text(stringResource(id = R.string.advance_setting))
                        },
                        supportingContent = {
                            Text(stringResource(id = R.string.set_number_format_locale_and_app_security))
                        },
                        leadingContent = {
                            NoRippleIconButton(
                                onClick = onSettingClicked,
                                interactionSource = interactionSource
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_setting),
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }
            }

            item {
                val interactionSource = remember { MutableInteractionSource() }

                Box(
                    modifier = Modifier
                        .clickable(
                            indication = rememberRipple(),
                            interactionSource = interactionSource,
                            onClick = onLanguageClicked
                        )
                ) {
                    ListItem(
                        headlineContent = {
                            Text(stringResource(id = R.string.language))
                        },
                        supportingContent = {
                            Text(language.name)
                        },
                        leadingContent = {
                            NoRippleIconButton(
                                onClick = onLanguageClicked,
                                interactionSource = interactionSource
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_global),
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .clickable(
                    indication = rememberRipple(),
                    interactionSource = signOutInteractionSource,
                    onClick = onSettingClicked
                )
        ) {
            ListItem(
                headlineContent = {
                    Text(stringResource(id = R.string.sign_out))
                },
                supportingContent = {
                    Text(email)
                },
                leadingContent = {
                    NoRippleIconButton(
                        onClick = onSignOutClicked,
                        interactionSource = signOutInteractionSource
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_logout),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    }
}
