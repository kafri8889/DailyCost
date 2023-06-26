package com.dcns.dailycost.data.datasource.local

import androidx.room.TypeConverter
import com.dcns.dailycost.data.CategoryIcon
import com.dcns.dailycost.data.WalletType

object DatabaseTypeConverter {

    @TypeConverter
    fun categoryIconToOrdinal(categoryIcon: CategoryIcon): Int = categoryIcon.ordinal

    @TypeConverter
    fun categoryIconFromOrdinal(ordinal: Int): CategoryIcon = CategoryIcon.values()[ordinal]

    @TypeConverter
    fun walletTypeToOrdinal(walletType: WalletType): Int = walletType.ordinal

    @TypeConverter
    fun walletTypeFromOrdinal(ordinal: Int): WalletType = WalletType.values()[ordinal]

}