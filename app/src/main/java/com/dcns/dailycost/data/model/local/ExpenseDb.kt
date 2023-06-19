package com.dcns.dailycost.data.model.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
@Entity(tableName = "expense_table")
data class ExpenseDb (
    @PrimaryKey
    @ColumnInfo(name = "pengeluaran_id") val id: String,
    @ColumnInfo(name = "nama") val name: String,
    @ColumnInfo(name = "tanggal") val date: LocalDate,
    @ColumnInfo(name = "jumlah") val amount: String,
    @ColumnInfo(name = "pembayaran") val payment: String,
    @ColumnInfo(name = "userId_expense") val userId: String,
): Parcelable