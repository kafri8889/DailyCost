package com.dcns.dailycost.navigation.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.ui.note.NoteScreen
import com.dcns.dailycost.ui.note.NoteViewModel

fun NavGraphBuilder.NoteNavigation(navigationActions: NavigationActions) {
    composable(
        route = TopLevelDestinations.Home.note.route
    ) { backEntry ->
        val mViewModel = hiltViewModel<NoteViewModel>(backEntry)

        NoteScreen(
            viewModel = mViewModel,
            navigationActions = navigationActions
        )
    }
}
