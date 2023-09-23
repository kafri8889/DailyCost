package com.dcns.dailycost.ui.app

import android.Manifest
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.dcns.dailycost.MainActivity
import com.dcns.dailycost.R
import com.dcns.dailycost.data.Language
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestination
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.data.TransactionType
import com.dcns.dailycost.data.defaultNavOptionsBuilder
import com.dcns.dailycost.data.drawerDestinations
import com.dcns.dailycost.foundation.base.BaseScreenWrapper
import com.dcns.dailycost.foundation.common.DailyCostBiometricManager
import com.dcns.dailycost.foundation.theme.DailyCostTheme
import com.dcns.dailycost.foundation.uicomponent.DrawerItem
import com.dcns.dailycost.navigation.HomeNavHost
import com.dcns.dailycost.navigation.LoginRegisterNavHost
import com.dcns.dailycost.navigation.OnboardingNavHost
import com.dcns.dailycost.navigation.home.CategoriesNavigation
import com.dcns.dailycost.navigation.home.CategoryNavigation
import com.dcns.dailycost.navigation.home.ChangeLanguageNavigation
import com.dcns.dailycost.navigation.home.ColorPickerNavigation
import com.dcns.dailycost.navigation.home.DashboardNavigation
import com.dcns.dailycost.navigation.home.IconPickerNavigation
import com.dcns.dailycost.navigation.home.NoteNavigation
import com.dcns.dailycost.navigation.home.NotesNavigation
import com.dcns.dailycost.navigation.home.NotificationNavigation
import com.dcns.dailycost.navigation.home.RecentActivityNavigation
import com.dcns.dailycost.navigation.home.SettingNavigation
import com.dcns.dailycost.navigation.home.StatisticNavigation
import com.dcns.dailycost.navigation.home.TransactionNavigation
import com.dcns.dailycost.navigation.home.TransactionsNavigation
import com.dcns.dailycost.navigation.home.WalletsNavigation
import com.dcns.dailycost.navigation.login_register.LoginNavigation
import com.dcns.dailycost.navigation.login_register.RegisterNavigation
import com.dcns.dailycost.navigation.onboarding.OnboardingNavigation
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import timber.log.Timber

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

	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
		RequestPostPermission()
	}

	DisposableEffect(navController) {
		val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
			destination.route?.let {
				viewModel.onAction(DailyCostAppAction.UpdateCurrentDestinationRoute(it))
			}
		}

		navController.addOnDestinationChangedListener(listener)
		onDispose {
			navController.removeOnDestinationChangedListener(listener)
		}
	}

	Observe(
		state = state,
		viewModel = viewModel,
		navActions = navActions,
		biometricManager = biometricManager
	)

	DailyCostTheme(darkTheme) {
		SideEffect {
			systemUiController.setSystemBarsColor(
				color = Color.Transparent,
				darkIcons = !darkTheme
			)
		}

		BaseScreenWrapper(viewModel) {
			Surface(color = MaterialTheme.colorScheme.background) {
				DailyCostDrawer(
					state = drawerState,
					email = state.userCredential?.email ?: "",
					language = state.language,
					onNavigationIconClicked = onNavigationIconClicked,
					onNavigateTo = { destination ->
						navActions.navigateTo(
							destination = destination,
							builder = defaultNavOptionsBuilder(
								popTo = TopLevelDestinations.Home.dashboard
							)
						)

						closeDrawer()
					},
					onSignOutClicked = {
						navActions.navigateTo(TopLevelDestinations.LoginRegister.login)
						closeDrawer()
					}
				) {
					DailyCostBottomSheetLayout(bottomSheetNavigator) {
						BackHandler(drawerState.isOpen || state.currentDestinationRoute == TopLevelDestinations.Home.dashboard.route) {
							when {
								drawerState.isOpen -> {
									scope.launch {
										drawerState.close()
									}
								}

								state.currentDestinationRoute == TopLevelDestinations.Home.dashboard.route -> {
									(context as MainActivity).finishAndRemoveTask()
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

}

/**
 * digunakan untuk mengamati perubahan pada state atau destinasi
 * kegunaan lebih lanjut bisa dilihat di comment
 */
@Composable
private fun Observe(
	viewModel: DailyCostAppViewModel,
	state: DailyCostAppState,
	biometricManager: DailyCostBiometricManager,
	navActions: NavigationActions
) {

	val context = LocalContext.current

	LaunchedEffect(state.isSecureAppEnabled, state.isBiometricAuthenticated) {
		val canAuth = DailyCostBiometricManager.canAuthenticateWithAuthenticators(context)

		// Jika user mengaktifkan secure app, dan
		// belum terautentikasi (awal membuka aplikasi), dan
		// device support biometric authentication
		if (state.isSecureAppEnabled && !state.isBiometricAuthenticated && canAuth) {
			// Tampilkan autentikasi
			biometricManager.showAuthentication(
				title = context.getString(R.string.authentication),
				subtitle = "",
				description = "",
				negativeButtonText = context.getString(R.string.cancel)
			)
		} else viewModel.onAction(DailyCostAppAction.IsBiometricAuthenticated(true))
	}

	// Navigasi otomatis
	LaunchedEffect(
		state.userCredential,
		state.isFirstInstall,
		state.userFirstEnteredApp,
		state.canNavigate
	) {
		// Jika user pertama kali masuk ke aplikasi, dan
		// first install tidak null (true atau false), dan
		// user credential tidak null.
		// Jika salah satunya null, maka user akan tetap berada di `SplashScreen`
		if (
			state.userFirstEnteredApp &&
			state.isFirstInstall != null &&
			state.userCredential != null &&
			state.canNavigate
		) {
			// Jika user telah login, biarkan, karena start destination ke dashboard
			if (state.userCredential.isLoggedIn) {
				return@LaunchedEffect
			}

			Timber.i("ruwt: user not logged in")

			val dest = when {
				// Jika pertama kali install, ke onboarding
				state.isFirstInstall == true -> TopLevelDestinations.Onboarding.onboarding
				// Jika tidak, ke login screen
				else -> TopLevelDestinations.LoginRegister.login
			}

			navActions.navigateTo(dest)

			// Update user pertama kali masuk ke aplikasi ke false
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
				is DailyCostAppUiEvent.HandleDeepLink -> {
					// Manipulasi uri
					val uri = when {
						event.uri.pathSegments.contains("pengeluaran") -> event.uri.toString().let { s ->
							"$s/${TransactionType.Expense}".toUri()
						}
						else -> event.uri
					}

					navActions.navigateTo(
						uri = uri,
						builder = defaultNavOptionsBuilder(
							popTo = TopLevelDestinations.Home.dashboard,
							inclusivePopUpTo = false
						)
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
		startDestination = TopLevelDestinations.Home.ROOT_ROUTE
	) {
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
			TransactionsNavigation(navActions)
			CategoryNavigation(navActions)
			RecentActivityNavigation(navActions)
			TransactionNavigation(navActions)
			CategoriesNavigation(navActions)
			StatisticNavigation(navActions)
			WalletsNavigation(navActions)
			NoteNavigation(navActions)
			NotificationNavigation(navActions)
			ColorPickerNavigation(navActions)
			IconPickerNavigation(navActions)

			DashboardNavigation(
				navigationActions = navActions,
				onNavigationIconClicked = onNavigationIconClicked
			)

			NotesNavigation(
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
	onSignOutClicked: () -> Unit,
	onNavigateTo: (TopLevelDestination) -> Unit,
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
				onNavigationIconClicked = onNavigationIconClicked,
				onSignOutClicked = onSignOutClicked,
				onNavigateTo = onNavigateTo
			)
		}
	)
}

@Composable
private fun DailyCostDrawerContent(
	email: String,
	language: Language,
	onNavigationIconClicked: () -> Unit,
	onSignOutClicked: () -> Unit,
	onNavigateTo: (TopLevelDestination) -> Unit
) {
	val context = LocalContext.current

	ModalDrawerSheet {
		LazyColumn(
			verticalArrangement = Arrangement.spacedBy(8.dp),
			modifier = Modifier
				.weight(1f)
		) {
			item {
				Box(
					modifier = Modifier
						.clickable(onClick = onNavigationIconClicked)
				) {
					DrawerItem(
						title = {
							Text(context.getString(R.string.dashboard))
						},
						icon = {
							Icon(
								painter = painterResource(id = R.drawable.ic_arrow_left),
								contentDescription = null
							)
						},
						modifier = Modifier
							.fillMaxWidth()
							.padding(
								start = 16.dp,
								bottom = 8.dp,
								top = 8.dp
							)
					)
				}
			}

			items(
				items = drawerDestinations
			) { destination ->
				Box(
					modifier = Modifier
						.clickable {
							onNavigateTo(destination)
						}
				) {
					DrawerItem(
						title = {
							Text(context.getString(destination.title!!))
						},
						summary = {
							Text(
								when (destination.route) {
									TopLevelDestinations.Home.changeLanguage.route -> language.name
									else -> context.getString(destination.subtitle!!)
								}
							)
						},
						icon = {
							Icon(
								painter = painterResource(id = destination.icon!!),
								contentDescription = null
							)
						},
						modifier = Modifier
							.fillMaxWidth()
							.padding(start = 16.dp)
					)
				}
			}
		}

		Box(
			modifier = Modifier
				.clickable(onClick = onSignOutClicked)
		) {
			DrawerItem(
				title = {
					Text(context.getString(R.string.sign_out))
				},
				summary = {
					Text(email)
				},
				icon = {
					Icon(
						painter = painterResource(id = R.drawable.ic_logout),
						contentDescription = null
					)
				},
				modifier = Modifier
					.fillMaxWidth()
					.padding(start = 16.dp)
			)
		}

		Spacer(modifier = Modifier.height(16.dp))
	}
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RequestPostPermission() {

	val permissionState = rememberPermissionState(
		permission = Manifest.permission.POST_NOTIFICATIONS
	)

	LaunchedEffect(Unit) {
		permissionState.launchPermissionRequest()
	}
}
