package com.dcns.dailycost.ui.transaction

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.R
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TransactionMode
import com.dcns.dailycost.data.TransactionType
import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.model.Category
import com.dcns.dailycost.foundation.base.BaseScreenWrapper
import com.dcns.dailycost.foundation.common.CommonDateFormatter
import com.dcns.dailycost.foundation.common.LocalCurrency
import com.dcns.dailycost.foundation.extension.dailyCostColor
import com.dcns.dailycost.foundation.extension.primaryLocale
import com.dcns.dailycost.foundation.theme.DailyCostTheme

@Composable
fun TransactionScreen(
    viewModel: TransactionViewModel,
    navigationActions: NavigationActions,
) {

    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    BaseScreenWrapper(
        viewModel = viewModel,
    ) { _ ->
        TransactionScreenContent(
            state = state,
            onNavigationIconClicked = navigationActions::popBackStack,
            onSaveClicked = {
                viewModel.onAction(TransactionAction.Save)
            },
            onDeleteTransaction = {
                viewModel.onAction(TransactionAction.Delete)
            },
            onTransactionTypeChanged = { type ->
                viewModel.onAction(TransactionAction.SetTransactionType(type))
            },
            onCategoryChanged = { category ->
                viewModel.onAction(TransactionAction.SetCategory(category))
            },
            onUsernameChanged = { username ->
                viewModel.onAction(TransactionAction.SetName(username))
            },
            onPaymentChanged = { walletType ->
                viewModel.onAction(TransactionAction.SetPayment(walletType))
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun TransactionScreenContent(
    state: TransactionState,
    modifier: Modifier = Modifier,
    onSaveClicked: () -> Unit,
    onDeleteTransaction: () -> Unit,
    onNavigationIconClicked: () -> Unit,
    onTransactionTypeChanged: (TransactionType) -> Unit,
    onCategoryChanged: (Category) -> Unit,
    onUsernameChanged: (String) -> Unit,
    onPaymentChanged: (WalletType) -> Unit,
    onAmountChanged: (Double) -> Unit,
    onDateChanged: (Long) -> Unit
) {

    val focusManager = LocalFocusManager.current

    val (
        nameFocusRequester,
        amountFocusRequester,
        paymentFocusRequester,
        dateFocusRequester,
        categoryFocusRequester
    ) = remember { FocusRequester.createRefs() }

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
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus(true)
                }
            }
    ) {
        item {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigationIconClicked) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            showDatePickerDialog = true
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_calendar),
                            contentDescription = stringResource(id = R.string.accessibility_pick_date)
                        )
                    }

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
                }
            )
        }

        if (state.transactionMode.isNew()) {
            item {
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                ) {
                    SegmentedButton(
                        shape = RoundedCornerShape(
                            topStartPercent = 100,
                            bottomStartPercent = 100
                        ),
                        selected = state.transactionType.isIncome,
                        icon = {},
                        onClick = {
                            onTransactionTypeChanged(TransactionType.Income)
                        }
                    ) {
                        Text(TransactionType.Income.name)
                    }

                    SegmentedButton(
                        shape = RoundedCornerShape(
                            topEndPercent = 100,
                            bottomEndPercent = 100
                        ),
                        selected = state.transactionType.isExpense,
                        icon = {},
                        onClick = {
                            onTransactionTypeChanged(TransactionType.Expense)
                        }
                    ) {
                        Text(TransactionType.Expense.name)
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = state.name,
                singleLine = true,
                onValueChange = onUsernameChanged,
                shape = RoundedCornerShape(20),
                colors = OutlinedTextFieldDefaults.dailyCostColor(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                label = {
                    Text("Name")
                },
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .focusRequester(nameFocusRequester)
            )
        }

        item {
            val currency = LocalCurrency.current

            val amount = remember(state.amount) {
                state.amount.toInt().toString()
            }

            OutlinedTextField(
                value = amount,
                singleLine = true,
                shape = RoundedCornerShape(20),
                colors = OutlinedTextFieldDefaults.dailyCostColor(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                prefix = {
                    Text(currency.symbol)
                },
                label = {
                    Text(stringResource(id = R.string.amount))
                },
                onValueChange = { n ->
                    val updatedAmount = n
                        .ifEmpty { "0" }
                        .filter { it.isDigit() }
                        .toDouble()

                    onAmountChanged(updatedAmount)
                },
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .focusRequester(amountFocusRequester)
            )
        }

        item {
            var expanded by remember { mutableStateOf(false) }

            LaunchedEffect(expanded) {
                if (!expanded) focusManager.clearFocus()
            }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                OutlinedTextField(
                    value = state.payment.name,
                    readOnly = true,
                    singleLine = true,
                    shape = RoundedCornerShape(20),
                    colors = OutlinedTextFieldDefaults.dailyCostColor(),
                    onValueChange = {},
                    label = {
                        Text("Payment")
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.ArrowDropDown,
                            contentDescription = stringResource(id = R.string.accessibility_expand_dropdown_menu),
                            modifier = Modifier
                                .composed {
                                    val degree by animateFloatAsState(
                                        label = "degree",
                                        targetValue = if (expanded) 540f else 0f,
                                        animationSpec = tween(256)
                                    )

                                    graphicsLayer {
                                        rotationZ = degree
                                    }
                                }
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(0.92f)
                        .focusRequester(paymentFocusRequester)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    for (type in WalletType.values()) {
                        DropdownMenuItem(
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            text = {
                                Text(type.localizedName)
                            },
                            onClick = {
                                onPaymentChanged(type)
                            }
                        )
                    }
                }
            }
        }

        item {
            var expanded by remember { mutableStateOf(false) }

            LaunchedEffect(expanded) {
                if (!expanded) focusManager.clearFocus()
            }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                OutlinedTextField(
                    value = state.category.name,
                    readOnly = true,
                    singleLine = true,
                    shape = RoundedCornerShape(20),
                    colors = OutlinedTextFieldDefaults.dailyCostColor(),
                    onValueChange = {},
                    label = {
                        Text("Category")
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.ArrowDropDown,
                            contentDescription = stringResource(id = R.string.accessibility_expand_dropdown_menu),
                            modifier = Modifier
                                .composed {
                                    val degree by animateFloatAsState(
                                        label = "degree",
                                        targetValue = if (expanded) 540f else 360f,
                                        animationSpec = tween(256)
                                    )

                                    graphicsLayer {
                                        rotationZ = degree
                                    }
                                }
                        )
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(0.92f)
                        .focusRequester(categoryFocusRequester)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    for (category in state.availableCategory) {
                        DropdownMenuItem(
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            text = {
                                Text(category.name)
                            },
                            onClick = {
                                onCategoryChanged(category)
                            }
                        )
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = CommonDateFormatter.edmy(LocalContext.current.primaryLocale).format(state.date),
                readOnly = true,
                singleLine = true,
                shape = RoundedCornerShape(20),
                colors = OutlinedTextFieldDefaults.dailyCostColor(),
                onValueChange = {},
                label = {
                    Text(stringResource(id = R.string.date))
                },
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .focusRequester(dateFocusRequester)
            )
        }

        item {
            Button(
                shape = RoundedCornerShape(25),
                onClick = onSaveClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DailyCostTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth(0.92f)
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}
