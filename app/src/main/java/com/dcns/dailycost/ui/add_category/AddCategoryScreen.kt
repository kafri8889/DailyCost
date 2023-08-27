package com.dcns.dailycost.ui.add_category

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.dcns.dailycost.R
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.foundation.base.BaseScreenWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryScreen(
	viewModel: AddCategoryViewModel,
	navigationActions: NavigationActions
) {

	BaseScreenWrapper(
		viewModel = viewModel,
		topBar = {
			TopAppBar(
				title = {
					Text(
						text = stringResource(id = R.string.add_category),
						style = MaterialTheme.typography.titleMedium
					)
				},
				navigationIcon = {
					IconButton(onClick = navigationActions::popBackStack) {
						Icon(
							painter = painterResource(id = R.drawable.ic_arrow_left),
							contentDescription = null
						)
					}
				},
				actions = {
					IconButton(
						onClick = {
							viewModel.onAction(AddCategoryAction.Save)
						}
					) {
						Icon(
							painter = painterResource(id = R.drawable.ic_check),
							contentDescription = stringResource(id = R.string.accessibility_save)
						)
					}
				}
			)
		}
	) { scaffoldPadding ->

	}
}
