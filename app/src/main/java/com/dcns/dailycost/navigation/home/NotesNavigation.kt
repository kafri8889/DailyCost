package com.dcns.dailycost.navigation.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.ui.notes.NoteScreen
import com.dcns.dailycost.ui.notes.NotesViewModel

fun NavGraphBuilder.NotesNavigation(
    navigationActions: NavigationActions,
    onNavigationIconClicked: () -> Unit
) {
    composable(
        route = TopLevelDestinations.Home.notes.route
    ) { backEntry ->
        val mViewModel = hiltViewModel<NotesViewModel>(backEntry)

        NoteScreen(
            viewModel = mViewModel,
            navigationActions = navigationActions,
            onNavigationIconClicked = onNavigationIconClicked
        )
    }
}
