package com.dcns.dailycost.foundation.uicomponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dcns.dailycost.data.datasource.local.LocalCategoryDataProvider
import com.dcns.dailycost.data.model.Category
import com.dcns.dailycost.foundation.theme.DailyCostTheme

@Preview
@Composable
private fun CategoryItemPreview() {
	DailyCostTheme {
		CategoryItem(
			category = LocalCategoryDataProvider.other,
			onClick = {}
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryItem(
	category: Category,
	modifier: Modifier = Modifier,
	onClick: () -> Unit
) {

	Card(
		onClick = onClick,
		modifier = modifier
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(8.dp),
			modifier = Modifier
				.padding(8.dp)
				.fillMaxWidth()
		) {
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.size(48.dp)
			) {
				Icon(
					painter = painterResource(id = category.icon.iconResId),
					contentDescription = null
				)
			}

			Text(category.name)
		}
	}
}
