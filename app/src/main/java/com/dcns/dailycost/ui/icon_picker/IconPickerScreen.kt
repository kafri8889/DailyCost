package com.dcns.dailycost.ui.icon_picker

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.R
import com.dcns.dailycost.data.CategoryIcon
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.foundation.theme.DailyCostTheme
import com.dcns.dailycost.navigation.home.shared.HomeSharedAction
import com.dcns.dailycost.navigation.home.shared.HomeSharedViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun IconPickerScreen(
	viewModel: IconPickerViewModel,
	sharedViewModel: HomeSharedViewModel,
	navigationActions: NavigationActions
) {

	val state by viewModel.state.collectAsStateWithLifecycle()
	val sharedState by sharedViewModel.state.collectAsStateWithLifecycle()

	LaunchedEffect(sharedState.selectedCategoryIcon) {
		if (sharedState.selectedCategoryIcon != CategoryIcon.Other) {
			viewModel.onAction(IconPickerAction.UpdateSelectedIcon(sharedState.selectedCategoryIcon))
		}
	}

	BackHandler {
		// Reset sharedState.selectedIcon before navigating
		sharedViewModel.onAction(HomeSharedAction.UpdateSelectedCategoryIcon(CategoryIcon.Other))
		navigationActions.popBackStack()
	}

	AlertDialog(onDismissRequest = navigationActions::popBackStack) {
		Surface(
			color = DailyCostTheme.colorScheme.wildSand,
			shape = RoundedCornerShape(8),
			modifier = Modifier
				.padding(vertical = 16.dp)
		) {
			Column(
				verticalArrangement = Arrangement.spacedBy(16.dp),
				modifier = Modifier
					.padding(16.dp)
			) {
				Text(
					text = stringResource(id = R.string.choose_icon),
					style = MaterialTheme.typography.titleMedium.copy(
						fontWeight = FontWeight.SemiBold
					)
				)

				FlowRow(
					verticalArrangement = Arrangement.spacedBy(4.dp),
					modifier = Modifier
						.verticalScroll(rememberScrollState())
						.weight(1f)
				) {
					for (categoryIcon in CategoryIcon.entries) {
						Box(
							contentAlignment = Alignment.Center,
							modifier = Modifier
								.size(48.dp)
								.clip(RoundedCornerShape(25))
								.background(if (categoryIcon == state.selectedIcon) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent)
								.clickable {
									sharedViewModel.onAction(
										HomeSharedAction.UpdateSelectedCategoryIcon(
											categoryIcon
										)
									)
									navigationActions.popBackStack()
								}
						) {
							Icon(
								painter = painterResource(categoryIcon.iconResId),
								contentDescription = null
							)
						}
					}
				}

				TextButton(
					onClick = navigationActions::popBackStack,
					colors = ButtonDefaults.textButtonColors(
						contentColor = DailyCostTheme.colorScheme.primary
					),
					modifier = Modifier
						.align(Alignment.End)
				) {
					Text(
						text = stringResource(id = R.string.cancel),
						style = MaterialTheme.typography.titleMedium.copy(
							fontWeight = FontWeight.SemiBold
						)
					)
				}
			}
		}
	}

}
