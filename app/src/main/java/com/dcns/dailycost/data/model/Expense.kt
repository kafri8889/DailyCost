package com.dcns.dailycost.data.model

import android.os.Parcelable
import com.dcns.dailycost.data.WalletType
import kotlinx.parcelize.Parcelize

@Parcelize
data class Expense(
    val id: Int,
    val userId: Int,
    val name: String,
    val amount: Double,
    val payment: WalletType,
    val date: Long,
    val category: Category
): Parcelable
