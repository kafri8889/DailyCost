package com.dcns.dailycost.ui.top_up

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.R
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.foundation.common.LocalCurrency
import com.dcns.dailycost.foundation.extension.toWallet
import com.dcns.dailycost.foundation.extension.toast
import com.dcns.dailycost.foundation.uicomponent.DragHandle
import com.dcns.dailycost.foundation.uicomponent.SelectableWalletItem

@Composable
fun TopUpScreen(
    viewModel: TopUpViewModel,
    navigationActions: NavigationActions
) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is TopUpUiEvent.TopUpSuccess -> {
                    if (state.internetConnectionAvailable) context.getString(R.string.top_up_success).toast(context)
                    navigationActions.popBackStack()
                }
                is TopUpUiEvent.TopUpFailed -> {
                    event.message.toast(context, Toast.LENGTH_LONG)
                }
            }
        }
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        item {
            DragHandle(
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        bottom = 8.dp
                    )
            )
        }

        items(WalletType.values()) { walletType ->
            SelectableWalletItem(
                wallet = state.balance.toWallet(walletType),
                selected = state.selectedWalletType == walletType,
                onClick = {
                    viewModel.onAction(TopUpAction.ChangeSelectedWalletType(walletType))
                },
                modifier = Modifier
                    .fillMaxWidth(0.96f)
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
                supportingText = if (!state.internetConnectionAvailable) {
                    {
                        Text(
                            stringResource(
                                id = R.string.no_internet_connection_x_to_server,
                                context.getString(R.string.top_up)
                            )
                        )
                    }
                } else null,
                onValueChange = { n ->
                    val updatedAmount = n
                        .ifEmpty { "0" }
                        .filter { it.isDigit() }
                        .toDouble()

                    viewModel.onAction(TopUpAction.ChangeWalletBalance(updatedAmount))
                },
                modifier = Modifier
                    .fillMaxWidth(0.96f)
            )
        }
        
        item { 
            Button(
                enabled = !state.isLoading,
                onClick = {
                    focusManager.clearFocus()
                    viewModel.onAction(TopUpAction.TopUp)
                },
                modifier = Modifier
                    .fillMaxWidth(0.96f)
            ) {
                Text(
                    text = buildString {
                        val text = stringResource(
                            id = if (state.isLoading) R.string.loading
                            else R.string.top_up
                        )

                        append(text)

                        if (state.isLoading) append("...")
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
