package com.dcns.dailycost.data.model

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import com.dcns.dailycost.R
import com.dcns.dailycost.data.CategoryIcon
import com.dcns.dailycost.data.datasource.local.LocalCategoryDataProvider
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
	val id: Int,
	val name: String,
	val icon: CategoryIcon,
	val colorArgb: Int = Color.Transparent.toArgb(),
	val defaultCategory: Boolean = false
): Parcelable {

	/**
	 * if this category is default return localized name, null otherwise
	 */
	val localizedNameForDefaultCategory: String?
		@Composable
		get() = when (id) {
			LocalCategoryDataProvider.Income.salary.id -> stringResource(id = R.string.salary)
			LocalCategoryDataProvider.Income.investment.id -> stringResource(id = R.string.investment)
			LocalCategoryDataProvider.Income.bonus.id -> stringResource(id = R.string.bonus)
			LocalCategoryDataProvider.Income.award.id -> stringResource(id = R.string.award)
			LocalCategoryDataProvider.Expense.food.id -> stringResource(id = R.string.food)
			LocalCategoryDataProvider.Expense.shopping.id -> stringResource(id = R.string.shopping)
			LocalCategoryDataProvider.Expense.transport.id -> stringResource(id = R.string.transport)
			LocalCategoryDataProvider.Expense.electronic.id -> stringResource(id = R.string.electronic)
			LocalCategoryDataProvider.Expense.entertainment.id -> stringResource(id = R.string.entertainment)
			LocalCategoryDataProvider.Expense.gadget.id -> stringResource(id = R.string.gadget)
			LocalCategoryDataProvider.other.id -> stringResource(id = R.string.other)
			else -> null
		}

}
