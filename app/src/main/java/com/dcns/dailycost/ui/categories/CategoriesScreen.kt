package com.dcns.dailycost.ui.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.R
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.foundation.base.BaseScreenWrapper

@Composable
fun CategoriesScreen(
    viewModel: CategoriesViewModel,
    navigationActions: NavigationActions
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    BaseScreenWrapper(
        viewModel = viewModel,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = navigationActions::popBackStack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { scaffoldPadding ->
        CategoriesScreenContent(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        )
    }
}

@Composable
private fun CategoriesScreenContent(
    state: CategoriesState,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {

    }
}
