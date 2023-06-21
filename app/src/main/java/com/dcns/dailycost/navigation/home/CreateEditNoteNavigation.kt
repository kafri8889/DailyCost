package com.dcns.dailycost.navigation.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.ui.create_edit_note.CreateEditNoteScreen
import com.dcns.dailycost.ui.create_edit_note.CreateEditNoteViewModel

fun NavGraphBuilder.CreateEditNoteNavigation(navigationActions: NavigationActions) {
    composable(
        route = TopLevelDestinations.Home.createEditNote.route
    ) { backEntry ->
        val mViewModel = hiltViewModel<CreateEditNoteViewModel>(backEntry)

        CreateEditNoteScreen(
            viewModel = mViewModel,
            navigationActions = navigationActions
        )
    }
}
