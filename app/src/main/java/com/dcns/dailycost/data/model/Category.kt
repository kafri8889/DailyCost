package com.dcns.dailycost.data.model

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dcns.dailycost.R
import com.dcns.dailycost.data.CategoryIcon
import com.dcns.dailycost.data.datasource.local.LocalCategoryDataProvider
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val id: Int,
    var name: String,
    var icon: CategoryIcon,
    var defaultCategory: Boolean = false
): Parcelable {

    /**
     * if this category is default return localized name, null otherwise
     */
    val localizedNameForDefaultCategory: String?
        @Composable
        get() = when (id) {
            LocalCategoryDataProvider.food.id -> stringResource(id = R.string.food)
            LocalCategoryDataProvider.shopping.id -> stringResource(id = R.string.shopping)
            LocalCategoryDataProvider.transport.id -> stringResource(id = R.string.transport)
            LocalCategoryDataProvider.electronic.id -> stringResource(id = R.string.electronic)
            LocalCategoryDataProvider.bill.id -> stringResource(id = R.string.bill)
            LocalCategoryDataProvider.entertainment.id -> stringResource(id = R.string.entertainment)
            LocalCategoryDataProvider.gadget.id -> stringResource(id = R.string.gadget)
            LocalCategoryDataProvider.other.id -> stringResource(id = R.string.other)
            else -> null
        }

}
