package com.dcns.dailycost.foundation.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

@Composable
fun CategoryItem(
	category: Category,
	modifier: Modifier = Modifier,
	trailing: @Composable (() -> Unit)? = null,
	onClick: () -> Unit
) {

	Card(
		colors = CardDefaults.cardColors(
			containerColor = Color.Transparent
		),
		modifier = Modifier
			.clickable { onClick() }
			.then(modifier)
	) {
		Row(
			horizontalArrangement = Arrangement.spacedBy(8.dp),
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.padding(8.dp)
				.fillMaxWidth()
		) {
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.clip(CircleShape)
					.size(40.dp)
					.background(Color(category.colorArgb))
			) {
				Icon(
					painter = painterResource(id = category.icon.iconResId),
					contentDescription = null,
					tint = MaterialTheme.colorScheme.onPrimary
				)
			}

			Text(
				text = category.name,
				modifier = Modifier
					.weight(1f)
			)

			trailing?.invoke()
		}
	}
}

@Composable
fun SelectableCategoryItem(
	category: Category,
	selected: Boolean,
	modifier: Modifier = Modifier,
	onClick: () -> Unit
) {

	CategoryItem(
		category = category,
		onClick = onClick,
		modifier = modifier,
		trailing = {
			RadioButton(
				selected = selected,
				onClick = onClick,
				colors = RadioButtonDefaults.colors(
					selectedColor = DailyCostTheme.colorScheme.primary
				)
			)
		}
	)
}
