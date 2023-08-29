package com.dcns.dailycost.ui.category

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.R
import com.dcns.dailycost.data.ActionMode
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.foundation.base.BaseScreenWrapper
import com.dcns.dailycost.foundation.uicomponent.DailyCostTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
	viewModel: CategoryViewModel,
	navigationActions: NavigationActions
) {
	val state by viewModel.state.collectAsStateWithLifecycle()

	BaseScreenWrapper(
		viewModel = viewModel,
		onEvent = { event ->
			if (event is CategoryUiEvent.Saved) {
				navigationActions.popBackStack()
			}
		},
		topBar = {
			TopAppBar(
				title = {
					Text(
						text = stringResource(
							id = when (state.actionMode) {
								ActionMode.New -> R.string.add_category
								ActionMode.Edit -> R.string.edit_category
								ActionMode.View -> R.string.view_category
							}
						),
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
					if (state.actionMode.isView() && !state.default) {
						IconButton(
							onClick = {

							}
						) {
							Icon(
								painter = painterResource(id = R.drawable.ic_trash),
								contentDescription = stringResource(id = R.string.accessibility_delete)
							)
						}
					}

					if (state.actionMode.isNew()) {
						IconButton(
							onClick = {
								viewModel.onAction(CategoryAction.Save)
							}
						) {
							Icon(
								painter = painterResource(id = R.drawable.ic_check),
								contentDescription = stringResource(id = R.string.accessibility_save)
							)
						}
					}
				}
			)
		}
	) { scaffoldPadding ->
		AddCategoryScreenContent(
			state = state,
			onNameChanged = { name ->
				viewModel.onAction(CategoryAction.SetName(name))
			},
			modifier = Modifier
				.fillMaxSize()
				.padding(scaffoldPadding)
		)
	}
}

@Composable
private fun AddCategoryScreenContent(
	state: CategoryState,
	modifier: Modifier = Modifier,
	onNameChanged: (String) -> Unit
) {

	val constraintSet = ConstraintSet {
		val (
			categoryIcon,
			nameTextField
		) = createRefsFor(
			"icon",
			"name"
		)

		val gl = createGuidelineFromBottom(0.5f)

		constrain(categoryIcon) {
			centerHorizontallyTo(parent)
			bottom.linkTo(nameTextField.top)
			top.linkTo(parent.top)
		}

		constrain(nameTextField) {
			centerHorizontallyTo(parent)
			top.linkTo(categoryIcon.bottom)
			bottom.linkTo(gl)
		}
	}

	ConstraintLayout(
		constraintSet = constraintSet,
		modifier = modifier
	) {
		Icon(
			painter = painterResource(id = state.icon.iconResId),
			contentDescription = null,
			modifier = Modifier
				.fillMaxWidth(0.32f)
				.aspectRatio(1f)
				.layoutId("icon")
		)

		DailyCostTextField(
			value = state.name,
			error = state.nameError,
			readOnly = state.actionMode.isView(),
			title = stringResource(id = R.string.category_name),
			errorText = stringResource(id = R.string.name_cant_be_empty),
			placeholder = stringResource(id = R.string.my_category),
			onValueChange = onNameChanged,
			modifier = Modifier
				.fillMaxWidth(0.92f)
				.layoutId("name")
		)
	}
}
