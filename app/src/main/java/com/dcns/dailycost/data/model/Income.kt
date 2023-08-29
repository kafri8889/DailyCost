package com.dcns.dailycost.data.model

import android.os.Parcelable
import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.foundation.common.SortableByDate
import com.dcns.dailycost.foundation.common.Transaction
import kotlinx.parcelize.Parcelize

@Parcelize
data class Income(
	override var id: Int,
	override val userId: Int,
	override val name: String,
	override val amount: Double,
	override val payment: WalletType,
	override val date: Long,
	override val category: Category
): Transaction, Parcelable, SortableByDate
