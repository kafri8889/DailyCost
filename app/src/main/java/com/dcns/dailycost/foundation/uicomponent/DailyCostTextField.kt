package com.dcns.dailycost.foundation.uicomponent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material3.ProvideTextStyle
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dcns.dailycost.foundation.theme.DailyCostTheme

object DailyCostTextFieldDefaults {

	@Composable
	fun IconButton(
		painter: Painter?,
		enabled: Boolean = true,
		onClick: () -> Unit
	) {
		IconButton(
			enabled = enabled,
			onClick = onClick
		) {
			if (painter != null) {
				Icon(
					painter = painter,
					contentDescription = null
				)
			}
		}
	}

}

@Composable
fun DailyCostTextField(
	modifier: Modifier = Modifier,
	focused: Boolean = false,
	textStyle: TextStyle = LocalTextStyle.current,
	error: Boolean = false,
	errorText: String = "",
	title: @Composable () -> Unit,
	content: @Composable () -> Unit,
	titleActionIcon: @Composable () -> Unit,
	leadingIcon: @Composable (RowScope.() -> Unit)? = null,
	trailingIcon: @Composable RowScope.() -> Unit = {
		DailyCostTextFieldDefaults.IconButton(
			enabled = false,
			onClick = {},
			painter = null,
		)
	}
) {
	Column(modifier = modifier) {
		Row(verticalAlignment = Alignment.CenterVertically) {
			ProvideTextStyle(
				content = title,
				value = MaterialTheme.typography.titleMedium.copy(
					fontWeight = FontWeight.SemiBold
				)
			)

			Spacer(modifier = Modifier.weight(1f))

			titleActionIcon()
		}

		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(8.dp)
		) {
			leadingIcon?.invoke(this)

			ProvideTextStyle(
				content = content,
				value = textStyle
			)

			trailingIcon()
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DailyCostTextField(
	title: String,
	value: String,
	modifier: Modifier = Modifier,
	textStyle: TextStyle = LocalTextStyle.current,
	placeholder: String? = null,
	titleActionIcon: Painter? = null,
	leadingIcon: @Composable (RowScope.() -> Unit)? = null,
	trailingIcon: @Composable RowScope.() -> Unit = {
		DailyCostTextFieldDefaults.IconButton(
			enabled = false,
			onClick = {},
			painter = null,
		)
	},
	readOnly: Boolean = false,
	singleLine: Boolean = false,
	error: Boolean = false,
	errorText: String = "",
	keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
	keyboardActions: KeyboardActions = KeyboardActions.Default,
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

			DailyCostTextFieldDefaults.IconButton(
				enabled = titleActionIcon != null,
				onClick = onTitleActionClicked,
				painter = titleActionIcon,
			)
		}

		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(8.dp)
		) {
			leadingIcon?.invoke(this)

			BasicTextField(
				value = value,
				readOnly = readOnly,
				textStyle = textStyle,
				singleLine = singleLine,
				onValueChange = onValueChange,
				keyboardActions = keyboardActions,
				keyboardOptions = keyboardOptions,
				cursorBrush = Brush.horizontalGradient(
					listOf(
						DailyCostTheme.colorScheme.primary,
						DailyCostTheme.colorScheme.primary
					)
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

			trailingIcon()
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
