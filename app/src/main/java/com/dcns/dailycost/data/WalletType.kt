package com.dcns.dailycost.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dcns.dailycost.R

enum class WalletType {
    Cash,
    EWallet,
    BankAccount;

    val localizedName: String
        @Composable
        get() = when (this) {
            Cash -> stringResource(id = R.string.cash)
            EWallet -> stringResource(id = R.string.e_wallet)
            BankAccount -> stringResource(id = R.string.bank_account)
        }

    val icon: Int
        @Composable
        get() = when (this) {
            Cash -> R.drawable.ic_money_3
            EWallet -> R.drawable.ic_bitcoin_card
            BankAccount -> R.drawable.ic_card
        }
}