package com.dcns.dailycost.ui.expense

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.foundation.base.BaseScreenWrapper

@Composable
fun ExpenseScreen(
    viewModel: ExpenseViewModel,
    navigationActions: NavigationActions
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    BaseScreenWrapper(viewModel = viewModel) { scaffoldPadding ->
        ExpenseScreenContent(
            modifier = Modifier
                .padding(scaffoldPadding)
        )
    }
}

@Composable
private fun ExpenseScreenContent(
    modifier: Modifier = Modifier
) {

}
