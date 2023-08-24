package com.dcns.dailycost.foundation.uicomponent

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.dcns.dailycost.data.Language
import com.dcns.dailycost.foundation.theme.DailyCostTheme

private class SelectablePreviewParameter: PreviewParameterProvider<Boolean> {
	override val values: Sequence<Boolean>
		get() = sequenceOf(true, false)
}

@Preview(showBackground = true)
@Composable
private fun LanguageItemPreview(
	@PreviewParameter(SelectablePreviewParameter::class) selected: Boolean
) {
	DailyCostTheme {
		LanguageItem(
			language = Language.English,
			selected = selected,
			onClick = {}
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageItem(
	language: Language,
	selected: Boolean,
	modifier: Modifier = Modifier,
	onClick: () -> Unit
) {

	val scaleAnimatable = remember { Animatable(1f) }

	LaunchedEffect(selected) {
		if (selected) {
			scaleAnimatable.animateTo(
				targetValue = 0.88f,
				animationSpec = spring(
					dampingRatio = Spring.DampingRatioMediumBouncy
				)
			)

			scaleAnimatable.animateTo(
				targetValue = 1f,
				animationSpec = spring(
					dampingRatio = Spring.DampingRatioMediumBouncy
				)
			)
		}
	}

	Card(
		onClick = onClick,
		border = if (!selected) BorderStroke(
			width = 1.dp,
			color = MaterialTheme.colorScheme.outline
		) else null,
		colors = CardDefaults.cardColors(
			containerColor = if (selected) MaterialTheme.colorScheme.primary
			else Color.Transparent
		),
		modifier = modifier
			.graphicsLayer {
				scaleX = scaleAnimatable.value
				scaleY = scaleAnimatable.value
			}
	) {
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp)
		) {
			Text(
				text = language.name,
				style = MaterialTheme.typography.titleMedium
			)
		}
	}
}
