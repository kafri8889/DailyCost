package com.dcns.dailycost.data.datasource.local

import androidx.room.TypeConverter
import com.dcns.dailycost.data.CategoryIcon

object DatabaseTypeConverter {

    @TypeConverter
    fun categoryIconToOrdinal(categoryIcon: CategoryIcon): Int = categoryIcon.ordinal

    @TypeConverter
    fun categoryIconFromOrdinal(ordinal: Int): CategoryIcon = CategoryIcon.values()[ordinal]

}