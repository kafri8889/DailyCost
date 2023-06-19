package com.dcns.dailycost.ui.note

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.R
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.foundation.base.BaseScreenWrapper
import com.dcns.dailycost.foundation.common.LocalDrawerState
import com.dcns.dailycost.foundation.uicomponent.NoteItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    viewModel: NoteViewModel,
    navigationActions: NavigationActions
) {

    val drawerState = LocalDrawerState.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    BackHandler {
        navigationActions.navigateTo(TopLevelDestinations.Home.dashboard)
    }

    BaseScreenWrapper(
        viewModel = viewModel,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        overflow = TextOverflow.Ellipsis,
                        text = stringResource(id = R.string.notes)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_menu),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { scaffoldPadding ->
        NoteScreenContent(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        )
    }
}

@Composable
private fun NoteScreenContent(
    state: NoteState,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        items(
            items = state.notes,
            key = { note -> note.id }
        ) { note ->
            NoteItem(
                note = note,
                onClick = {

                },
                modifier = Modifier
                    .fillMaxWidth(0.96f)
            )
        }
    }
}
