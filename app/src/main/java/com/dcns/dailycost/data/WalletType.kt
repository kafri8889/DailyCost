package com.dcns.dailycost.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dcns.dailycost.R

enum class WalletType {
	Cash,
	EWallet,
	BankAccount;

	/**
	 * Digunakan di request body saat melakukan permintaan ke API
	 */
	val apiName: String
		get() = when (this) {
			Cash -> "CASH"
			EWallet -> "GOPAY"
			BankAccount -> "REKENING"
		}

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

	companion object {
		fun parse(input: String): WalletType {
			return when (input.lowercase()) {
				"cash" -> Cash
				"gopay" -> EWallet
				"rekening" -> BankAccount
				else -> throw IllegalArgumentException("Invalid wallet for input: $input")
			}
		}
	}
}