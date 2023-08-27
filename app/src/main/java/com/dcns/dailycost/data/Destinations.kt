package com.dcns.dailycost.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.dcns.dailycost.R
import com.dcns.dailycost.foundation.nav_type.ActionModeNavType
import com.dcns.dailycost.foundation.nav_type.TransactionTypeNavType

object DestinationRoute {
	const val CHANGE_LANGUAGE = "change_language"
	const val TRANSACTIONS = "transactions"
	const val TRANSACTION = "transaction"
	const val CATEGORIES = "categories"
	const val ONBOARDING = "onboarding"
	const val DASHBOARD = "dashboard"
	const val REGISTER = "register"
	const val CATEGORY = "category"
	const val SETTING = "setting"
	const val SPLASH = "splash"
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
	const val ACTION_MODE = "transaction_mode"
}

data class TopLevelDestination(
	val route: String,
	val arguments: List<NamedNavArgument> = emptyList(),
	@StringRes val name: Int? = null,
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

class NavigationActions(private val navController: NavHostController) {

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
		inclusive: Boolean = false,
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
			route = DestinationRoute.LOGIN
		)

		val register = TopLevelDestination(
			route = DestinationRoute.REGISTER
		)
	}

	object Home {
		const val ROOT_ROUTE = "root_home"

		val changeLanguage = TopLevelDestination(
			route = DestinationRoute.CHANGE_LANGUAGE
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

		val categories = TopLevelDestination(
			route = DestinationRoute.CATEGORIES
		)

		val dashboard = TopLevelDestination(
			route = DestinationRoute.DASHBOARD,
			icon = R.drawable.ic_dashboard,
			name = R.string.dashboard
		)

		val setting = TopLevelDestination(
			route = DestinationRoute.SETTING,
			icon = R.drawable.ic_setting,
			name = R.string.advance_setting
		)

		val notes = TopLevelDestination(
			route = DestinationRoute.NOTES,
			icon = R.drawable.ic_notes,
			name = R.string.notes
		)

		val note = TopLevelDestination(
			route = DestinationRoute.NOTE
		)
	}

	val splash = TopLevelDestination(
		route = DestinationRoute.SPLASH
	)

}

val drawerDestinations = arrayOf(
	TopLevelDestinations.Home.dashboard,
	TopLevelDestinations.Home.notes,
	TopLevelDestinations.Home.setting,
)
