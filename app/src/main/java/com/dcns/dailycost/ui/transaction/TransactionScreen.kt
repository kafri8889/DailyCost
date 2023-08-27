package com.dcns.dailycost.ui.transaction

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.R
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestination
import com.dcns.dailycost.data.TransactionMode
import com.dcns.dailycost.data.TransactionType
import com.dcns.dailycost.foundation.base.BaseScreenWrapper
import com.dcns.dailycost.foundation.common.CommonDateFormatter
import com.dcns.dailycost.foundation.common.LocalCurrency
import com.dcns.dailycost.foundation.extension.primaryLocale
import com.dcns.dailycost.foundation.theme.DailyCostTheme
import com.dcns.dailycost.foundation.uicomponent.TransactionSegmentedButton

@Composable
fun TransactionScreen(
	viewModel: TransactionViewModel,
	navigationActions: NavigationActions,
) {

	val state by viewModel.state.collectAsStateWithLifecycle()

	BaseScreenWrapper(
		viewModel = viewModel,
		onEvent = { event ->
			when (event) {
				is TransactionUiEvent.TransactionDeleted, is TransactionUiEvent.TransactionSaved -> {
					navigationActions.popBackStack()
				}
			}
		}
	) { _ ->
		TransactionScreenContent(
			state = state,
			onNavigationIconClicked = navigationActions::popBackStack,
			onNavigateTo = { destination ->
				navigationActions.navigateTo(destination)
			},
			onSaveClicked = {
				viewModel.onAction(TransactionAction.Save)
			},
			onDeleteTransaction = {
				viewModel.onAction(TransactionAction.Delete)
			},
			onTransactionTypeChanged = { type ->
				viewModel.onAction(TransactionAction.SetTransactionType(type))
			},
			onTitleChanged = { username ->
				viewModel.onAction(TransactionAction.SetName(username))
			},
			onAmountChanged = { amount ->
				viewModel.onAction(TransactionAction.SetAmount(amount))
			},
			onDateChanged = { date ->
				viewModel.onAction(TransactionAction.SetDate(date))
			},
			modifier = Modifier
				.systemBarsPadding()
				.fillMaxSize()
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionScreenContent(
	state: TransactionState,
	modifier: Modifier = Modifier,
	onSaveClicked: () -> Unit,
	onDeleteTransaction: () -> Unit,
	onNavigationIconClicked: () -> Unit,
	onNavigateTo: (TopLevelDestination) -> Unit,
	onTransactionTypeChanged: (TransactionType) -> Unit,
	onTitleChanged: (String) -> Unit,
	onAmountChanged: (Double) -> Unit,
	onDateChanged: (Long) -> Unit
) {

	val focusManager = LocalFocusManager.current

	var showDatePickerDialog by remember { mutableStateOf(false) }
	var showDeleteDialog by remember { mutableStateOf(false) }

	if (showDatePickerDialog) {
		val datePickerState = rememberDatePickerState(
			initialSelectedDateMillis = state.date
		)

		DatePickerDialog(
			onDismissRequest = {
				showDatePickerDialog = false
			},
			confirmButton = {
				Button(
					enabled = datePickerState.selectedDateMillis != null,
					onClick = {
						onDateChanged(datePickerState.selectedDateMillis!!)
						showDatePickerDialog = false
					}
				) {
					Text(stringResource(id = R.string.ok))
				}
			},
			dismissButton = {
				TextButton(
					onClick = {
						showDatePickerDialog = false
					}
				) {
					Text(stringResource(id = R.string.close))
				}
			}
		) {
			DatePicker(state = datePickerState)
		}
	}

	if (showDeleteDialog) {
		AlertDialog(
			onDismissRequest = {
				showDeleteDialog = false
			},
			icon = {
				Icon(
					painter = painterResource(id = R.drawable.ic_trash),
					contentDescription = null
				)
			},
			title = {
				Text(stringResource(id = R.string.delete_transaction))
			},
			text = {
				Text(stringResource(id = R.string.are_you_sure_you_want_to_delete_this_transaction))
			},
			confirmButton = {
				Button(
					onClick = {
						showDeleteDialog = false
						onDeleteTransaction()
					}
				) {
					Text(stringResource(id = R.string.delete))
				}
			},
			dismissButton = {
				TextButton(
					onClick = {
						showDeleteDialog = false
					}
				) {
					Text(stringResource(id = R.string.cancel))
				}
			}
		)
	}

	LazyColumn(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(16.dp),
		modifier = modifier
			.pointerInput(Unit) {
				detectTapGestures {
					focusManager.clearFocus(true)
				}
			}
	) {
		item {
			TopAppBar(
				title = {
					Text(
						text = stringResource(
							id = if (state.transactionMode.isNew()) R.string.new_transaction else R.string.view_transaction
						),
						style = MaterialTheme.typography.titleMedium
					)
				},
				navigationIcon = {
					IconButton(onClick = onNavigationIconClicked) {
						Icon(
							painter = painterResource(id = R.drawable.ic_arrow_left),
							contentDescription = null
						)
					}
				},
				actions = {
					if (state.transactionMode == TransactionMode.Edit) {
						IconButton(
							onClick = {
								showDeleteDialog = true
							}
						) {
							Icon(
								painter = painterResource(id = R.drawable.ic_trash),
								contentDescription = stringResource(id = R.string.accessibility_delete_transaction)
							)
						}
					}

					if (state.transactionMode == TransactionMode.New) {
						IconButton(onClick = onSaveClicked) {
							Icon(
								painter = painterResource(id = R.drawable.ic_check),
								contentDescription = stringResource(id = R.string.accessibility_save)
							)
						}
					}
				}
			)
		}

		if (state.transactionMode.isNew()) {
			item {
				TransactionSegmentedButton(
					selectedTransactionType = state.transactionType,
					onTransactionTypeChanged = onTransactionTypeChanged,
					modifier = Modifier
						.fillMaxWidth(0.92f)
						.clip(RoundedCornerShape(25))
						.background(DailyCostTheme.colorScheme.primaryContainer.copy(alpha = 0.48f))
				)
			}
		}

		item {
			TransactionTextField(
				placeholder = stringResource(id = R.string.buy_food),
				title = stringResource(id = R.string.title),
				value = state.title,
				readOnly = state.transactionMode.isEdit(),
				singleLine = true,
				error = state.titleError,
				errorText = stringResource(id = R.string.title_cant_be_empty),
				onValueChange = onTitleChanged,
				keyboardOptions = KeyboardOptions(
					imeAction = ImeAction.Done,
					keyboardType = KeyboardType.Text
				),
				keyboardActions = KeyboardActions(
					onDone = {
						focusManager.clearFocus()
					}
				),
				modifier = Modifier
					.fillMaxWidth(0.92f)
			)
		}

		item {
			TransactionTextField(
				title = stringResource(id = R.string.wallet),
				value = state.payment.name,
				readOnly = true,
				singleLine = true,
				titleActionIcon = if (state.transactionMode.isNew()) painterResource(id = R.drawable.ic_arrow_down) else null,
				onValueChange = {},
				onTitleActionClicked = {
					// TODO: Navigate to wallet selector screen
				},
				modifier = Modifier
					.fillMaxWidth(0.92f)
			)
		}

		item {
			TransactionTextField(
				title = stringResource(id = R.string.category),
				value = state.category.name,
				readOnly = true,
				singleLine = true,
				titleActionIcon = if (state.transactionMode.isNew()) painterResource(id = R.drawable.ic_arrow_down) else null,
				onValueChange = {},
				onTitleActionClicked = {
					// TODO: Navigate to category selector screen
				},
				modifier = Modifier
					.fillMaxWidth(0.92f)
			)
		}

		item {
			TransactionTextField(
				title = stringResource(id = R.string.date),
				value = CommonDateFormatter.edmy(LocalContext.current.primaryLocale).format(state.date),
				readOnly = true,
				singleLine = true,
				trailingIcon = if (state.transactionMode.isNew()) painterResource(id = R.drawable.ic_calendar) else null,
				onValueChange = {},
				onTrailingIconClicked = {
					showDatePickerDialog = true
				},
				modifier = Modifier
					.fillMaxWidth(0.92f)
			)
		}

		item {
			val amount = remember(state.amount) {
				state.amount.toInt().toString().replace("(\\d)(?=(\\d{3})+\$)".toRegex(), "$1.")
			}

			Column(
				verticalArrangement = Arrangement.spacedBy(8.dp),
				modifier = Modifier
					.fillMaxWidth(0.92f)
			) {
				Text(
					text = stringResource(id = R.string.amount),
					style = MaterialTheme.typography.titleMedium.copy(
						fontWeight = FontWeight.SemiBold
					)
				)

				Row(
					verticalAlignment = Alignment.Bottom,
					modifier = Modifier
						.fillMaxWidth()
				) {
					AnimatedContent(
						label = "transaction icon",
						targetState = state.transactionType,
						transitionSpec = {
							scaleIn(tween(256)) togetherWith scaleOut(tween(256))
						}
					) { type ->
						Icon(
							imageVector = if (type.isIncome) Icons.Rounded.Add else Icons.Rounded.Remove,
							contentDescription = null,
							modifier = Modifier
								.size(32.dp)
						)
					}

					BasicTextField(
						value = amount,
						readOnly = state.transactionMode.isEdit(),
						cursorBrush = Brush.horizontalGradient(
							listOf(
								DailyCostTheme.colorScheme.primary,
								DailyCostTheme.colorScheme.primary
							)
						),
						textStyle = MaterialTheme.typography.displaySmall.copy(
							textAlign = TextAlign.End
						),
						keyboardOptions = KeyboardOptions(
							keyboardType = KeyboardType.Number,
							imeAction = ImeAction.Done
						),
						keyboardActions = KeyboardActions(
							onDone = {
								focusManager.clearFocus()
							}
						),
						onValueChange = { n ->
							val updatedAmount = n
								.ifEmpty { "0" }
								.filter { it.isDigit() }
								.toDouble()
								.coerceAtMost(1_000_000_000.0)

							onAmountChanged(updatedAmount)
						},
						modifier = Modifier
							.weight(1f)
					)

					Spacer(modifier = Modifier.width(4.dp))

					Text(
						text = LocalCurrency.current.countryCode,
						style = MaterialTheme.typography.titleLarge
					)
				}

				HorizontalDivider(color = DailyCostTheme.colorScheme.outline)
			}
		}

		item {
			Spacer(modifier = Modifier.height(24.dp))
		}
	}
}

@Composable
private fun TransactionTextField(
	title: String,
	value: String,
	modifier: Modifier = Modifier,
	placeholder: String? = null,
	focusRequester: FocusRequester? = null,
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
	
	val focusRequesterModifier = if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier
	val usePlaceholder by rememberUpdatedState(newValue = placeholder != null && value.isBlank())

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
					.then(focusRequesterModifier)
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

		HorizontalDivider(color = if (error) MaterialTheme.colorScheme.error else DailyCostTheme.colorScheme.outline)
		
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
