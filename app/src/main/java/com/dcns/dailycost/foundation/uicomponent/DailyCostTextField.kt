package com.dcns.dailycost.foundation.uicomponent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dcns.dailycost.foundation.theme.DailyCostTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DailyCostTextField(
	title: String,
	value: String,
	modifier: Modifier = Modifier,
	placeholder: String? = null,
	titleActionIcon: Painter? = null,
	trailingIcon: Painter? = null,
	readOnly: Boolean = false,
	singleLine: Boolean = false,
	error: Boolean = false,
	errorText: String = "",
	keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
	keyboardActions: KeyboardActions = KeyboardActions.Default,
	onTrailingIconClicked: () -> Unit = {},
	onTitleActionClicked: () -> Unit = {},
	onValueChange: (String) -> Unit
) {

	val focusManager = LocalFocusManager.current

	val usePlaceholder = placeholder != null && value.isBlank()
	val isImeVisible = WindowInsets.isImeVisible

	var focused by remember { mutableStateOf(false) }

	LaunchedEffect(isImeVisible) {
		if (!isImeVisible) focusManager.clearFocus(true)
	}

	Column(modifier = modifier) {
		Row(verticalAlignment = Alignment.CenterVertically) {
			Text(
				text = title,
				style = MaterialTheme.typography.titleMedium.copy(
					fontWeight = FontWeight.SemiBold
				)
			)

			Spacer(modifier = Modifier.weight(1f))

			IconButton(
				enabled = titleActionIcon != null,
				onClick = onTitleActionClicked
			) {
				if (titleActionIcon != null) {
					Icon(
						painter = titleActionIcon,
						contentDescription = null
					)
				}
			}
		}

		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(8.dp)
		) {
			BasicTextField(
				value = value,
				readOnly = readOnly,
				singleLine = singleLine,
				onValueChange = onValueChange,
				keyboardActions = keyboardActions,
				keyboardOptions = keyboardOptions,
				textStyle = MaterialTheme.typography.titleMedium.copy(
					fontWeight = FontWeight.Normal
				),
				decorationBox = { innerTextField ->
					Box {
						if (usePlaceholder && placeholder != null) {
							Text(
								text = placeholder,
								style = LocalTextStyle.current.copy(color = LocalTextStyle.current.color.copy(alpha = 0.48f))
							)
						}

						innerTextField()
					}
				},
				modifier = Modifier
					.weight(1f)
					.then(modifier)
					.onFocusChanged { state ->
						focused = state.isFocused
					}
			)

			IconButton(
				enabled = trailingIcon != null,
				onClick = onTrailingIconClicked
			) {
				if (trailingIcon != null) {
					Icon(
						painter = trailingIcon,
						contentDescription = null
					)
				}
			}
		}

		HorizontalDivider(
			color = when {
				error -> MaterialTheme.colorScheme.error
				focused -> DailyCostTheme.colorScheme.primary
				else -> DailyCostTheme.colorScheme.outline
			}
		)

		Spacer(modifier = Modifier.height(8.dp))

		AnimatedVisibility(
			visible = error,
			enter = fadeIn(),
			exit = fadeOut(),
			modifier = Modifier
				.align(Alignment.End)
		) {
			Text(
				text = errorText,
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.error
				)
			)
		}
	}
}
