package com.dcns.dailycost.data.model.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dcns.dailycost.data.WalletType
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "income_table")
data class IncomeDb(
    @PrimaryKey
    @ColumnInfo(name = "id_income") val id: Int,
    @ColumnInfo(name = "userId_income") val userId: Int,
    @ColumnInfo(name = "name_income") val name: String,
    @ColumnInfo(name = "amount_income") val amount: Double,
    @ColumnInfo(name = "payment_income") val payment: WalletType,
    @ColumnInfo(name = "categoryId_income") val categoryId: Int,
    /**
     * Time in millis
     */
    @ColumnInfo(name = "date_income") val date: Long,
): Parcelable
