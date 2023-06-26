package com.dcns.dailycost.data.datasource.local

import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.model.Income

object LocalIncomeDataProvider {

    val income1 = Income(
        id = 0,
        userId = 73,
        name = "Income 1",
        amount = 0.0,
        payment = WalletType.Cash,
        date = System.currentTimeMillis(),
        category = LocalCategoryDataProvider.other
    )

    val income2 = Income(
        id = 1,
        userId = 73,
        name = "Income 2",
        amount = 1_000_000.0,
        payment = WalletType.EWallet,
        date = System.currentTimeMillis(),
        category = LocalCategoryDataProvider.Income.salary
    )

    val values = arrayOf(
        income1,
        income2
    )

}