package com.dcns.dailycost.foundation.nav_type

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.dcns.dailycost.data.TransactionType

val NavType.Companion.TransactionTypeNavType: NavType<TransactionType>
    get() = object : NavType<TransactionType>(true) {
        override val name: String
            get() = "transaction_type"

        override fun get(bundle: Bundle, key: String): TransactionType? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(key, TransactionType::class.java)
            } else bundle.getParcelable(key)
        }

        override fun parseValue(value: String): TransactionType {
            return TransactionType.values()[value.toInt()]
        }

        override fun put(bundle: Bundle, key: String, value: TransactionType) {
            bundle.putInt(key, value.ordinal)
        }
    }
