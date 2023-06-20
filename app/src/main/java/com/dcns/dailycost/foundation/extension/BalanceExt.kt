package com.dcns.dailycost.foundation.extension

import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.model.UserBalance
import com.dcns.dailycost.data.model.Wallet

fun UserBalance.toWallet(type: WalletType): Wallet {
    return when (type) {
        WalletType.Cash -> Wallet(type, cash)
        WalletType.EWallet -> Wallet(type, eWallet)
        WalletType.BankAccount -> Wallet(type, bankAccount)
    }
}