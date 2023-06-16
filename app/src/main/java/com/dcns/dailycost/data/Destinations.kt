package com.dcns.dailycost.data

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController

object DestinationRoute {
    const val LOGIN_REGISTER = "login_register"
}

/**
 * Key untuk argument
 */
object DestinationArgument {

}

data class TopLevelDestination(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    /**
     * @param value {key: value}
     */
    fun createRoute(vararg value: Pair<Any, Any>): TopLevelDestination {
        var mRoute = route

        value.forEach { (key, value) ->
            mRoute = mRoute.replace("{$key}", value.toString())
        }

        return TopLevelDestination(mRoute, arguments)
    }
}

class NavigationActions(private val navController: NavHostController) {

    fun popBackStack() = navController.popBackStack()

    fun navigateTo(destination: TopLevelDestination) {
        navController.navigate(destination.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
//			popUpTo(navController.graph.findStartDestination().id) {
//				saveState = true
//			}
            // Avoid multiple copies of the same destination when
            // re-selecting the same item
            launchSingleTop = true
            // Restore state when re-selecting a previously selected item
            restoreState = true
        }
    }
}

object TopLevelDestinations {

    object LoginRegister {
        const val ROOT_ROUTE = "root_login_register"

        val loginRegister = TopLevelDestination(
            route = DestinationRoute.LOGIN_REGISTER
        )
    }

    object Home {
        const val ROOT_ROUTE = "root_home"

    }

}
