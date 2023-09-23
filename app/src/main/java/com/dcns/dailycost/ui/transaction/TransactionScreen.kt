package com.dcns.dailycost.ui.transaction

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.R
import com.dcns.dailycost.data.ActionMode
import com.dcns.dailycost.data.CategoriesScreenMode
import com.dcns.dailycost.data.DestinationArgument
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.data.TransactionType
import com.dcns.dailycost.data.WalletsScreenMode
import com.dcns.dailycost.foundation.base.BaseScreenWrapper
import com.dcns.dailycost.foundation.common.CommonDateFormatter
import com.dcns.dailycost.foundation.common.LocalCurrency
import com.dcns.dailycost.foundation.extension.primaryLocale
import com.dcns.dailycost.foundation.theme.DailyCostTheme
import com.dcns.dailycost.foundation.uicomponent.DailyCostTextField
import com.dcns.dailycost.foundation.uicomponent.DailyCostTextFieldDefaults
import com.dcns.dailycost.foundation.uicomponent.TransactionSegmentedButton
import com.dcns.dailycost.navigation.home.shared.HomeSharedAction
import com.dcns.dailycost.navigation.home.shared.HomeSharedViewModel

@Composable
fun TransactionScreen(
	viewModel: TransactionViewModel,
	sharedViewModel: HomeSharedViewModel,
	navigationActions: NavigationActions,
) {

	val state by viewModel.state.collectAsStateWithLifecycle()
	val sharedState by sharedViewModel.state.collectAsStateWithLifecycle()

	LaunchedEffect(sharedState.selectedWalletType) {
		sharedState.selectedWalletType?.let {
			viewModel.onAction(TransactionAction.SetPayment(it))
		}
	}

	LaunchedEffect(sharedState.selectedCategory) {
		sharedState.selectedCategory?.let {
			viewModel.onAction(TransactionAction.SetCategory(it))
		}
	}

	BackHandler {
		// Reset sharedState.selectedIcon before navigating
		sharedViewModel.onAction(HomeSharedAction.UpdateSelectedCategory(null))
		sharedViewModel.onAction(HomeSharedAction.UpdateSelectedWalletType(null))
		navigationActions.popBackStack()
	}

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
			onSelectCategory = {
				sharedViewModel.onAction(HomeSharedAction.UpdateSelectedCategory(state.category))
				navigationActions.navigateTo(
					TopLevelDestinations.Home.categories.createRoute(
						DestinationArgument.CATEGORIES_SCREEN_MODE to CategoriesScreenMode.SelectCategory
					)
				)
			},
			onSelectWallet = {
				sharedViewModel.onAction(HomeSharedAction.UpdateSelectedWalletType(state.payment))
				navigationActions.navigateTo(
					TopLevelDestinations.Home.wallets.createRoute(
						DestinationArgument.WALLETS_SCREEN_MODE to WalletsScreenMode.SelectWallet
					)
				)
			},
			onSaveClicked = {
				viewModel.onAction(TransactionAction.Save)
				sharedViewModel.onAction(HomeSharedAction.UpdateSelectedCategory(null))
				sharedViewModel.onAction(HomeSharedAction.UpdateSelectedWalletType(null))
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
	onSelectWallet: () -> Unit,
	onSelectCategory: () -> Unit,
	onDeleteTransaction: () -> Unit,
	onNavigationIconClicked: () -> Unit,
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
			tonalElevation = 0.dp,
			colors = DatePickerDefaults.colors(
				containerColor = Color(0xffF4F4F4)
			),
			onDismissRequest = {
				showDatePickerDialog = false
			},
			confirmButton = {
				Button(
					enabled = datePickerState.selectedDateMillis != null,
					colors = ButtonDefaults.buttonColors(
						containerColor = DailyCostTheme.colorScheme.primary
					),
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
					colors = ButtonDefaults.textButtonColors(
						contentColor = DailyCostTheme.colorScheme.primary
					),
					onClick = {
						showDatePickerDialog = false
					}
				) {
					Text(stringResource(id = R.string.close))
				}
			}
		) {
			DatePicker(
				state = datePickerState,
				colors = DatePickerDefaults.colors(
					selectedYearContainerColor = DailyCostTheme.colorScheme.primary,
					selectedDayContainerColor = DailyCostTheme.colorScheme.primary,
					todayDateBorderColor = DailyCostTheme.colorScheme.primary,
					todayContentColor = DailyCostTheme.colorScheme.primary
				)
			)
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
							id = if (state.actionMode.isNew()) R.string.new_transaction else R.string.view_transaction
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
					if (state.actionMode == ActionMode.View) {
						IconButton(
							onClick = {
								showDeleteDialog = true
							}
						) {
							Icon(
								painter = painterResource(id = R.drawable.ic_trash),
								contentDescription = stringResource(id = R.string.accessibility_delete)
							)
						}
					}

					if (state.actionMode == ActionMode.New) {
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

		if (state.actionMode.isNew()) {
			item {
				TransactionSegmentedButton(
					selectedTransactionType = state.selectedTransactionType,
					onTransactionTypeChanged = onTransactionTypeChanged,
					modifier = Modifier
						.fillMaxWidth(0.92f)
				)
			}
		}

		item {
			DailyCostTextField(
				placeholder = stringResource(id = R.string.buy_food),
				title = stringResource(id = R.string.title),
				value = state.title,
				readOnly = state.actionMode.isView(),
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
			DailyCostTextField(
				title = stringResource(id = R.string.wallet),
				value = state.payment.name,
				readOnly = true,
				singleLine = true,
				titleActionIcon = if (state.actionMode.isNew()) painterResource(id = R.drawable.ic_arrow_down) else null,
				onValueChange = {},
				onTitleActionClicked = onSelectWallet,
				modifier = Modifier
					.fillMaxWidth(0.92f)
			)
		}

		item {
			DailyCostTextField(
				title = stringResource(id = R.string.category),
				value = state.category.name,
				readOnly = true,
				singleLine = true,
				titleActionIcon = if (state.actionMode.isNew()) painterResource(id = R.drawable.ic_arrow_down) else null,
				onValueChange = {},
				onTitleActionClicked = onSelectCategory,
				modifier = Modifier
					.fillMaxWidth(0.92f)
			)
		}

		item {
			DailyCostTextField(
				title = stringResource(id = R.string.date),
				value = CommonDateFormatter.edmy(LocalContext.current.primaryLocale).format(state.date),
				readOnly = true,
				singleLine = true,
				trailingIcon = {
					val icon = if (state.actionMode.isNew()) painterResource(
						id = R.drawable.ic_calendar
					) else null

					DailyCostTextFieldDefaults.IconButton(
						enabled = icon != null,
						painter = icon,
						onClick = {
							showDatePickerDialog = true
						}
					)
				},
				onValueChange = {},
				modifier = Modifier
					.fillMaxWidth(0.92f)
			)
		}

		item {
			val amount = remember(state.amount) {
				// Menambahkan titik setiap 3 digit angka
				state.amount.toInt().toString().replace("(\\d)(?=(\\d{3})+\$)".toRegex(), "$1.")
			}

			DailyCostTextField(
				title = stringResource(id = R.string.amount),
				value = amount,
				readOnly = state.actionMode.isView(),
				singleLine = true,
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
				leadingIcon = {
					AnimatedContent(
						label = "transaction icon",
						targetState = state.selectedTransactionType,
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
				},
				trailingIcon = {
					Text(
						text = LocalCurrency.current.countryCode,
						style = MaterialTheme.typography.titleLarge,
						modifier = Modifier
							.align(Alignment.Bottom)
					)
				},
				onValueChange = { n ->
					val updatedAmount = n
						.ifEmpty { "0" }
						.filter { it.isDigit() }
						.toDouble()
						.coerceAtMost(1_000_000_000.0)

					onAmountChanged(updatedAmount)
				},
				modifier = Modifier
					.fillMaxWidth(0.92f)
			)
		}

		item {
			Spacer(modifier = Modifier.height(24.dp))
		}
	}
}
