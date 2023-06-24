/*
 Creator: Anaf Naufalian
 Github: https://github.com/kafri8889
 Twitter: https://twitter.com/anafthdev_
*/

package com.dcns.dailycost.foundation.uicomponent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun GroupPreference(
	title: String,
	modifier: Modifier = Modifier,
	withIconSpace: Boolean = false,
	content: @Composable () -> Unit
) {

	Column(
		modifier = modifier
	) {
		Row {
			Box(
				modifier = Modifier.weight(
					weight = if (withIconSpace) 0.12f else 0.03f
				)
			)

			Text(
				text = title,
				style = MaterialTheme.typography.titleMedium.copy(
					color = MaterialTheme.colorScheme.primary,
					fontWeight = FontWeight.Bold
				),
				modifier = Modifier
					.weight(0.88f)
			)
		}

		Spacer(modifier = Modifier.height(8.dp))

		content()
	}
}

@Composable
fun BasicPreference(
	showValue: Boolean = true,
	icon: @Composable (() -> Unit)? = null,
	value: @Composable ((textAlign: TextAlign) -> Unit)? = null,
	summary: @Composable ((maxLines: Int, overflow: TextOverflow) -> Unit)? = null,
	title: @Composable () -> Unit,
	onClick: () -> Unit
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxWidth()
			.height(64.dp)
			.clickable { onClick() }
	) {
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.weight(0.12f)
		) {
			icon?.invoke()
		}

		Column(
			verticalArrangement = Arrangement.Center,
			modifier = Modifier
				.weight(0.68f)
		) {
			ProvideTextStyle(
				MaterialTheme.typography.titleMedium.copy(
					fontWeight = FontWeight.Medium
				)
			) {
				title()
			}

			if (summary != null) {
				ProvideTextStyle(
					MaterialTheme.typography.titleSmall.copy(
						fontWeight = FontWeight.Light
					)
				) {
					summary(2, TextOverflow.Ellipsis)
				}
			}
		}

		Box(
			contentAlignment = Alignment.CenterEnd,
			modifier = Modifier
				.padding(end = 12.dp)
				.weight(0.2f)
		) {
			if (showValue && value != null) {
				ProvideTextStyle(
					MaterialTheme.typography.titleMedium.copy(
						fontWeight = FontWeight.Normal,
						color = MaterialTheme.colorScheme.primary
					)
				) {
					value(TextAlign.End)
				}
			}
		}
	}
}

@Composable
fun SwitchPreference(
	title: @Composable () -> Unit,
	isChecked: Boolean,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	onCheckedChange: (Boolean) -> Unit,
	summary: @Composable () -> Unit = {},
	icon: @Composable (() -> Unit)? = null,
	switch: @Composable () -> Unit = {
		Switch(
			enabled = enabled,
			checked = isChecked,
			onCheckedChange = {
				onCheckedChange(!isChecked)
			}
		)
	}
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = modifier
			.heightIn(min = 64.dp)
			.clickable(
				enabled = enabled,
				onClick = {
					onCheckedChange(!isChecked)
				}
			)
	) {
		CompositionLocalProvider(
			LocalContentColor provides LocalContentColor.current.copy(
				alpha = if (enabled) 1f else 0.32f
			)
		) {
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.weight(
						weight = 0.12f
					)
			) {
				icon?.invoke()
			}

			Column(
				verticalArrangement = Arrangement.Center,
				modifier = Modifier
					.weight(0.68f)
			) {
				ProvideTextStyle(
					value = MaterialTheme.typography.titleMedium.copy(
						fontWeight = FontWeight.Medium
					)
				) {
					title()
				}

				ProvideTextStyle(
					value = MaterialTheme.typography.titleSmall.copy(
						color = Color.Gray,
						fontWeight = FontWeight.Normal
					)
				) {
					summary()
				}
			}
		}

		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.weight(0.2f)
		) {
			switch()
		}
	}
}

