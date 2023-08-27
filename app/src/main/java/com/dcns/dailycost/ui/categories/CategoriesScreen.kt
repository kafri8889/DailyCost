package com.dcns.dailycost.ui.categories

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.R
import com.dcns.dailycost.data.ActionMode
import com.dcns.dailycost.data.DestinationArgument
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.data.model.Category
import com.dcns.dailycost.foundation.base.BaseScreenWrapper
import com.dcns.dailycost.foundation.theme.DailyCostTheme
import com.dcns.dailycost.foundation.uicomponent.CategoryItem

@OptIn(ExperimentalMaterial3Api::class)
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
				title = {
					Text(stringResource(id = R.string.categories))
				},
				navigationIcon = {
					IconButton(onClick = navigationActions::popBackStack) {
						Icon(
							painter = painterResource(id = R.drawable.ic_arrow_left),
							contentDescription = null
						)
					}
				}
			)
		},
		floatingActionButton = {
			FloatingActionButton(
				shape = CircleShape,
				containerColor = DailyCostTheme.colorScheme.primary,
				onClick = {
					navigationActions.navigateTo(TopLevelDestinations.Home.category)
				}
			) {
				Icon(
					painter = painterResource(id = R.drawable.ic_add),
					contentDescription = null,
					tint = DailyCostTheme.colorScheme.onPrimary
				)
			}
		}
	) { scaffoldPadding ->
		CategoriesScreenContent(
			state = state,
			onCategoryClicked = { category ->
				navigationActions.navigateTo(
					TopLevelDestinations.Home.category.createRoute(
						DestinationArgument.CATEGORY_ID to category.id,
						DestinationArgument.ACTION_MODE to ActionMode.View
					)
				)
			},
			modifier = Modifier
				.fillMaxSize()
				.padding(scaffoldPadding)
		)
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CategoriesScreenContent(
	state: CategoriesState,
	modifier: Modifier = Modifier,
	onCategoryClicked: (Category) -> Unit
) {

	LazyColumn(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier
	) {
		items(
			items = state.categories,
			key = { item -> item.id }
		) { category ->
			CategoryItem(
				category = category,
				onClick = {
					onCategoryClicked(category)
				},
				modifier = Modifier
					.fillMaxWidth()
					.animateItemPlacement(tween(256))
			)
		}
	}
}
