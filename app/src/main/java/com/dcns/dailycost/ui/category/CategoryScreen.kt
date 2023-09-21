package com.dcns.dailycost.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.R
import com.dcns.dailycost.data.ActionMode
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestination
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.foundation.base.BaseScreenWrapper
import com.dcns.dailycost.foundation.uicomponent.DailyCostTextField
import com.dcns.dailycost.foundation.uicomponent.DailyCostTextFieldDefaults
import com.dcns.dailycost.navigation.home.shared.HomeSharedState
import com.dcns.dailycost.navigation.home.shared.HomeSharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
	viewModel: CategoryViewModel,
	sharedViewModel: HomeSharedViewModel,
	navigationActions: NavigationActions
) {
	val state by viewModel.state.collectAsStateWithLifecycle()
	val sharedState by sharedViewModel.state.collectAsStateWithLifecycle()

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
					if (state.actionMode.isView() && !state.category.defaultCategory) {
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
			sharedState = sharedState,
			onNameChanged = { name ->
				viewModel.onAction(CategoryAction.SetName(name))
			},
			onNavigateTo = { dest ->
				navigationActions.navigateTo(dest)
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
	sharedState: HomeSharedState,
	modifier: Modifier = Modifier,
	onNameChanged: (String) -> Unit,
	onNavigateTo: (TopLevelDestination) -> Unit
) {

	val constraintSet = ConstraintSet {
		val (
			editIconButton,
			colorTextField,
			nameTextField,
			iconBox,
		) = createRefsFor(
			"editIconButton",
			"colorTextField",
			"nameTextField",
			"iconBox"
		)

		val gl = createGuidelineFromBottom(0.5f)

		constrain(iconBox) {
			centerHorizontallyTo(parent)
			bottom.linkTo(nameTextField.top)
			top.linkTo(parent.top)
		}

		constrain(nameTextField) {
			centerHorizontallyTo(parent)
			top.linkTo(iconBox.bottom)
			bottom.linkTo(colorTextField.top) //
		}

		constrain(colorTextField) {
			centerHorizontallyTo(parent)
			top.linkTo(nameTextField.bottom)
			bottom.linkTo(gl)
		}

		constrain(editIconButton) {
			top.linkTo(iconBox.top)
			start.linkTo(iconBox.end, 16.dp)
		}
	}

	ConstraintLayout(
		constraintSet = constraintSet,
		modifier = modifier
	) {
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.widthIn(max = 384.dp)
				.fillMaxWidth(0.32f)
				.aspectRatio(1f)
				.clip(CircleShape)
				.background(Color.Yellow)
				.layoutId("iconBox")
		) {
			Icon(
				painter = painterResource(id = state.category.icon.iconResId),
				contentDescription = null
			)
		}

		IconButton(
			onClick = {

			},
			modifier = Modifier
				.layoutId("editIconButton")
		) {
			Icon(
				painter = painterResource(id = R.drawable.ic_edit_with_underline),
				contentDescription = null
			)
		}

		DailyCostTextField(
			value = state.category.name,
			error = state.nameError,
			readOnly = state.actionMode.isView(),
			title = stringResource(id = R.string.category_name),
			errorText = stringResource(id = R.string.name_cant_be_empty),
			placeholder = stringResource(id = R.string.my_category),
			onValueChange = onNameChanged,
			modifier = Modifier
				.fillMaxWidth(0.92f)
				.layoutId("nameTextField")
		)

		DailyCostTextField(
			title = {
				Text(stringResource(id = R.string.color))
			},
			content = {
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.height(24.dp)
						.background(Color(sharedState.selectedArgbColor))
				)
			},
			titleActionIcon = {
				DailyCostTextFieldDefaults.IconButton(
					painter = painterResource(id = R.drawable.ic_arrow_down),
					onClick = {
						onNavigateTo(TopLevelDestinations.Home.colorPicker)
					},
				)
			},
			modifier = Modifier
				.fillMaxWidth(0.92f)
				.layoutId("colorTextField")
		)
	}
}
