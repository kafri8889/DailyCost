package com.dcns.dailycost.data.model.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dcns.dailycost.data.CategoryIcon
import kotlinx.parcelize.Parcelize

@Entity(tableName = "category_table")
@Parcelize
data class CategoryDb(
	@PrimaryKey @ColumnInfo(name = "category_id") val id: Int,
	@ColumnInfo(name = "category_name") var name: String,
	@ColumnInfo(name = "category_icon") var icon: CategoryIcon,
	/**
	 * ARGB color int
	 */
	@ColumnInfo(name = "category_color") var color: Int,
	@ColumnInfo(name = "category_defaultCategory") var defaultCategory: Boolean = false
): Parcelable
