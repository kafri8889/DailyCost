package com.dcns.dailycost.ui.transaction

import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.datasource.local.LocalCategoryDataProvider
import com.dcns.dailycost.data.model.Category

data class TransactionState(
    val name: String = "",
    val amount: Double = 0.0,
    val payment: WalletType = WalletType.Cash,
    val date: Long = System.currentTimeMillis(),
    val category: Category = LocalCategoryDataProvider.other

)
