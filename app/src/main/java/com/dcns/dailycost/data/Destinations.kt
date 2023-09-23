package com.dcns.dailycost.data

import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.navOptions
import com.dcns.dailycost.R
import com.dcns.dailycost.foundation.nav_type.ActionModeNavType
import com.dcns.dailycost.foundation.nav_type.CategoriesScreenModeNavType
import com.dcns.dailycost.foundation.nav_type.TransactionTypeNavType
import com.dcns.dailycost.foundation.nav_type.WalletsScreenModeNavType

object DestinationRoute {
	const val CHANGE_LANGUAGE = "change_language"
	const val RECENT_ACTIVITY = "recent_activity"
	const val COLOR_PICKER = "color_picker"
	const val NOTIFICATION = "notification"
	const val TRANSACTIONS = "transactions"
	const val ICON_PICKER = "icon_picker"
	const val TRANSACTION = "transaction"
	const val CATEGORIES = "categories"
	const val ONBOARDING = "onboarding"
	const val DASHBOARD = "dashboard"
	const val STATISTIC = "statistic"
	const val REGISTER = "register"
	const val CATEGORY = "category"
	const val WALLETS = "wallets"
	const val SETTING = "setting"
	const val LOGIN = "login"
	const val NOTES = "notes"
	const val NOTE = "note"
}

/**
 * Key untuk argument
 */
object DestinationArgument {
	const val TRANSACTION_ID = "transaction_id"
	const val TRANSACTION_TYPE = "transaction_type"
	const val CATEGORY_ID = "category_id"
	const val WALLET_ID = "wallet_id"
	const val ACTION_MODE = "action_mode"
	const val CATEGORIES_SCREEN_MODE = "category_screen_mode"
	const val WALLETS_SCREEN_MODE = "wallets_screen_mode"
}

data class TopLevelDestination(
	val route: String,
	val arguments: List<NamedNavArgument> = emptyList(),
	val deepLinks: List<NavDeepLink> = emptyList(),
	@StringRes val title: Int? = null,
	@StringRes val subtitle: Int? = null,
	@DrawableRes val icon: Int? = null
) {
	/**
	 * @param value {key: value}
	 */
	fun createRoute(vararg value: Pair<Any, Any?>): TopLevelDestination {
		var mRoute = route

		value.forEach { (key, value) ->
			mRoute = mRoute.replace("{$key}", value.toString())
		}

		return TopLevelDestination(mRoute, arguments)
	}
}

class NavigationActions(val navController: NavHostController) {

	fun popBackStack() = navController.popBackStack()

	fun popBackStack(
		destinationId: Int,
		inclusive: Boolean = false
	) = navController.popBackStack(
		destinationId,
		inclusive
	)

	fun popBackStack(
		destinationId: Int,
		inclusive: Boolean = true,
		saveState: Boolean = false
	) = navController.popBackStack(
		destinationId,
		inclusive,
		saveState
	)

	fun popBackStack(
		route: String,
		inclusive: Boolean = false,
		saveState: Boolean = false
	) = navController.popBackStack(
		route,
		inclusive,
		saveState
	)

	fun navigateTo(
		destination: TopLevelDestination,
		builder: NavOptionsBuilder.() -> Unit = defaultNavOptionsBuilder()
	) {
		navController.navigate(destination.route, builder)
	}

	fun navigateTo(
		uri: Uri,
		builder: NavOptionsBuilder.() -> Unit = defaultNavOptionsBuilder()
	) {
		navController.navigate(uri, navOptions(builder))
	}
}

fun defaultNavOptionsBuilder(
	popTo: TopLevelDestination? = null,
	launchAsSingleTop: Boolean = true,
	saveStatePopUpTo: Boolean = true,
	inclusivePopUpTo: Boolean = false,
	restoreAnyState: Boolean = true
): NavOptionsBuilder.() -> Unit = {
	// Pop up to the start destination of the graph to
	// avoid building up a large stack of destinations
	// on the back stack as users select items
	popTo?.let {
		popUpTo(popTo.route) {
			saveState = saveStatePopUpTo
			inclusive = inclusivePopUpTo
		}
	}
	// Avoid multiple copies of the same destination when
	// re-selecting the same item
	launchSingleTop = launchAsSingleTop
	// Restore state when re-selecting a previously selected item
	restoreState = restoreAnyState
}

object TopLevelDestinations {

	object Onboarding {
		const val ROOT_ROUTE = "root_onboarding"

		val onboarding = TopLevelDestination(
			route = DestinationRoute.ONBOARDING
		)
	}

	object LoginRegister {
		const val ROOT_ROUTE = "root_login_register"

		val login = TopLevelDestination(
			route = DestinationRoute.LOGIN,
			deepLinks = listOf(
				navDeepLink {
					action = Intent.ACTION_VIEW
					uriPattern = "${Constant.APP_DEEP_LINK_SCHEME}://${Constant.APP_DEEP_LINK_HOST}/${DestinationRoute.LOGIN}"
				}
			)
		)

		val register = TopLevelDestination(
			route = DestinationRoute.REGISTER
		)
	}

	object Home {
		const val ROOT_ROUTE = "root_home"

		val changeLanguage = TopLevelDestination(
			route = DestinationRoute.CHANGE_LANGUAGE,
			icon = R.drawable.ic_language,
			title = R.string.language
		)

		/**
		 * Required argument:
		 * - [DestinationArgument.TRANSACTION_TYPE]
		 */
		val transactions = TopLevelDestination(
			route = "${DestinationRoute.TRANSACTIONS}?" +
				"${DestinationArgument.TRANSACTION_TYPE}={${DestinationArgument.TRANSACTION_TYPE}}",
			arguments = listOf(
				navArgument(DestinationArgument.TRANSACTION_TYPE) {
					type = NavType.TransactionTypeNavType
					nullable = true
				}
			),
			deepLinks = listOf(
				navDeepLink {
					action = Intent.ACTION_VIEW
					uriPattern = "${Constant.WEB_DEEP_LINK_SCHEME}://${Constant.WEB_DEEP_LINK_HOST}/pengeluaran/{${DestinationArgument.TRANSACTION_TYPE}}"
				}
			)
		)

		/**
		 * Required argument:
		 * - [DestinationArgument.TRANSACTION_ID]
		 * - [DestinationArgument.ACTION_MODE]
		 * - [DestinationArgument.TRANSACTION_TYPE]
		 */
		val transaction = TopLevelDestination(
			route = "${DestinationRoute.TRANSACTION}?" +
				"${DestinationArgument.TRANSACTION_ID}={${DestinationArgument.TRANSACTION_ID}}&" +
				"${DestinationArgument.ACTION_MODE}={${DestinationArgument.ACTION_MODE}}&" +
				"${DestinationArgument.TRANSACTION_TYPE}={${DestinationArgument.TRANSACTION_TYPE}}",
			arguments = listOf(
				navArgument(DestinationArgument.TRANSACTION_ID) {
					type = NavType.IntType
					defaultValue = -1
				},
				navArgument(DestinationArgument.ACTION_MODE) {
					type = NavType.ActionModeNavType
					nullable = true
					defaultValue = ActionMode.New
				},
				navArgument(DestinationArgument.TRANSACTION_TYPE) {
					type = NavType.TransactionTypeNavType
					nullable = true
					defaultValue = TransactionType.Income
				}
			)
		)

		/**
		 * Required argument:
		 * - [DestinationArgument.CATEGORY_ID]
		 * - [DestinationArgument.ACTION_MODE]
		 */
		val category = TopLevelDestination(
			route = "${DestinationRoute.CATEGORY}?" +
				"${DestinationArgument.CATEGORY_ID}={${DestinationArgument.CATEGORY_ID}}&" +
				"${DestinationArgument.ACTION_MODE}={${DestinationArgument.ACTION_MODE}}",
			arguments = listOf(
				navArgument(DestinationArgument.CATEGORY_ID) {
					type = NavType.IntType
					defaultValue = -1
				},
				navArgument(DestinationArgument.ACTION_MODE) {
					type = NavType.ActionModeNavType
					nullable = true
					defaultValue = ActionMode.New
				}
			)
		)

		/**
		 * Required argument:
		 * - [DestinationArgument.CATEGORIES_SCREEN_MODE]
		 */
		val categories = TopLevelDestination(
			icon = R.drawable.ic_pie_chart,
			title = R.string.categories,
			subtitle = R.string.manage_categories_change_icon_color,
			route = "${DestinationRoute.CATEGORIES}?" +
				"${DestinationArgument.CATEGORIES_SCREEN_MODE}={${DestinationArgument.CATEGORIES_SCREEN_MODE}}",
			arguments = listOf(
				navArgument(DestinationArgument.CATEGORIES_SCREEN_MODE) {
					type = NavType.CategoriesScreenModeNavType
					defaultValue = CategoriesScreenMode.CategoryList
				}
			)
		)

		/**
		 * Required argument:
		 * - [DestinationArgument.WALLETS_SCREEN_MODE]
		 */
		val wallets = TopLevelDestination(
			route = "${DestinationRoute.WALLETS}?" +
				"${DestinationArgument.WALLETS_SCREEN_MODE}={${DestinationArgument.WALLETS_SCREEN_MODE}}",
			arguments = listOf(
				navArgument(DestinationArgument.WALLETS_SCREEN_MODE) {
					type = NavType.WalletsScreenModeNavType
					defaultValue = WalletsScreenMode.WalletList
				}
			)
		)

		val statistic = TopLevelDestination(
			route = DestinationRoute.STATISTIC,
			icon = R.drawable.ic_diagram,
			title = R.string.statistic,
			subtitle = R.string.your_financial_activities_in_graphical
		)

		val recentActivity = TopLevelDestination(
			route = DestinationRoute.RECENT_ACTIVITY,
			icon = R.drawable.ic_recent_activity,
			title = R.string.recent_activity,
			subtitle = R.string.keep_track_of_all_your
		)

		val dashboard = TopLevelDestination(
			route = DestinationRoute.DASHBOARD,
			icon = R.drawable.ic_dashboard,
			title = R.string.dashboard,
			deepLinks = listOf(
				navDeepLink {
					action = Intent.ACTION_VIEW
					uriPattern = "${Constant.WEB_DEEP_LINK_SCHEME}://${Constant.WEB_DEEP_LINK_HOST}/dashboard"
				}
			)
		)

		val setting = TopLevelDestination(
			route = DestinationRoute.SETTING,
			icon = R.drawable.ic_setting,
			title = R.string.advance_setting,
			subtitle = R.string.set_number_format_locale_and_app_security
		)

		val notes = TopLevelDestination(
			route = DestinationRoute.NOTES,
			icon = R.drawable.ic_notes,
			title = R.string.notes,
			subtitle = R.string.record_all_your_financial,
			deepLinks = listOf(
				navDeepLink {
					action = Intent.ACTION_VIEW
					uriPattern = "${Constant.WEB_DEEP_LINK_SCHEME}://${Constant.WEB_DEEP_LINK_HOST}/catatan"
				}
			)
		)

		val note = TopLevelDestination(
			route = DestinationRoute.NOTE
		)

		val notification = TopLevelDestination(
			route = DestinationRoute.NOTIFICATION,
			deepLinks = listOf(
				navDeepLink {
					action = Intent.ACTION_VIEW
					uriPattern = "${Constant.APP_DEEP_LINK_SCHEME}://${Constant.APP_DEEP_LINK_HOST}/${DestinationRoute.NOTIFICATION}"
				}
			)
		)

		val colorPicker = TopLevelDestination(
			route = DestinationRoute.COLOR_PICKER
		)

		val iconPicker = TopLevelDestination(
			route = DestinationRoute.ICON_PICKER
		)
	}

}

val drawerDestinations = arrayOf(
	TopLevelDestinations.Home.recentActivity,
	TopLevelDestinations.Home.statistic,
	TopLevelDestinations.Home.categories,
	TopLevelDestinations.Home.notes,
	TopLevelDestinations.Home.setting,
	TopLevelDestinations.Home.changeLanguage,
)
