package com.dcns.dailycost.ui.dashboard

import androidx.compose.runtime.Composable
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.foundation.base.BaseScreenWrapper

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    navigationActions: NavigationActions
) {

    BaseScreenWrapper(viewModel) { scaffoldPadding ->

    }
}
