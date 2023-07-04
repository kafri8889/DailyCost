package com.dcns.dailycost.ui.transactions

import com.dcns.dailycost.data.TransactionType
import com.dcns.dailycost.foundation.common.Transaction

data class TransactionsState(
    /**
     * Kalo null, semua transaksi (income & expense)
     */
    val selectedTransactionType: TransactionType? = null,
    val transactions: List<Transaction> = emptyList(),
)
