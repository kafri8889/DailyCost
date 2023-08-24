package com.dcns.dailycost.data.model.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dcns.dailycost.data.WalletType
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "expense_table")
data class ExpenseDb(
	@PrimaryKey
	@ColumnInfo(name = "id_expense") val id: Int,
	@ColumnInfo(name = "userId_expense") val userId: Int,
	@ColumnInfo(name = "name_expense") val name: String,
	@ColumnInfo(name = "amount_expense") val amount: Double,
	@ColumnInfo(name = "payment_expense") val payment: WalletType,
	@ColumnInfo(name = "categoryId_expense") val categoryId: Int,
	/**
	 * Time in millis
	 */
	@ColumnInfo(name = "date_expense") val date: Long
): Parcelable