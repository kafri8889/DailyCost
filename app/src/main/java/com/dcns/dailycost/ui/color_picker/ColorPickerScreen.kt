package com.dcns.dailycost.ui.color_picker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.R
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.foundation.theme.DailyCostTheme
import com.dcns.dailycost.navigation.home.shared.HomeSharedAction
import com.dcns.dailycost.navigation.home.shared.HomeSharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPickerScreen(
	viewModel: ColorPickerViewModel,
	sharedViewModel: HomeSharedViewModel,
	navigationActions: NavigationActions
) {

	val state by viewModel.state.collectAsStateWithLifecycle()

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
					text = stringResource(id = R.string.choose_color),
					style = MaterialTheme.typography.titleMedium.copy(
						fontWeight = FontWeight.SemiBold
					)
				)

				LazyColumn(
					verticalArrangement = Arrangement.spacedBy(12.dp),
					modifier = Modifier
						.weight(1f, false)
				) {
					items(state.argbColors) { argb ->
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.height(36.dp)
								.background(Color(argb))
								.clickable {
									sharedViewModel.onAction(HomeSharedAction.UpdateSelectedArgbColor(argb))
									navigationActions.popBackStack()
								}
						)
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
