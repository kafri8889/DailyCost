package com.dcns.dailycost.data.datasource.local

import androidx.room.TypeConverter
import com.dcns.dailycost.data.CategoryIcon
import com.dcns.dailycost.data.WalletType

/**
 * Type converter untuk database
 */
object DatabaseTypeConverter {

	/**
	 * Convert category icon ke ordinal (enum index)
	 */
	@TypeConverter
	@JvmStatic
	fun categoryIconToOrdinal(categoryIcon: CategoryIcon): Int = categoryIcon.ordinal

	/**
	 * Convert ordninal (enum index) ke category icon
	 */
	@TypeConverter
	@JvmStatic
	fun categoryIconFromOrdinal(ordinal: Int): CategoryIcon = CategoryIcon.entries[ordinal]

	/**
	 * Convert wallet type ke ordinal (enum index)
	 */
	@TypeConverter
	@JvmStatic
	fun walletTypeToOrdinal(walletType: WalletType): Int = walletType.ordinal

	/**
	 * Convert ordninal (enum index) ke wallet tipe
	 */
	@TypeConverter
	@JvmStatic
	fun walletTypeFromOrdinal(ordinal: Int): WalletType = WalletType.entries[ordinal]

}