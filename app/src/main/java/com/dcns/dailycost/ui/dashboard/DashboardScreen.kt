package com.dcns.dailycost.ui.dashboard

import androidx.activity.compose.BackHandler
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.dcns.dailycost.MainActivity
import com.dcns.dailycost.R
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.foundation.base.BaseScreenWrapper
import com.dcns.dailycost.foundation.common.LocalDrawerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    navigationActions: NavigationActions
) {

    val context = LocalContext.current
    val drawerState = LocalDrawerState.current

    val scope = rememberCoroutineScope()

    // Exit app
    BackHandler {
        (context as MainActivity).finishAndRemoveTask()
    }

    BaseScreenWrapper(
        viewModel = viewModel,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(id = R.string.dashboard))
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

    }
}
