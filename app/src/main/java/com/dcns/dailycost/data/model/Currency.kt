package com.dcns.dailycost.data.model

import android.os.Parcelable
import com.dcns.dailycost.data.CurrencyCode
import kotlinx.parcelize.Parcelize

@Parcelize
data class Currency(
    val name: String,
    val country: String,
    val countryCode: String,
    val symbol: String
): Parcelable {

    companion object {
        val rupiah = with(java.util.Currency.getInstance(CurrencyCode.IDR.name)) {
            Currency(
                name = "Rupiah",
                country = displayName,
                countryCode = currencyCode,
                symbol = symbol
            )
        }

        val availableCurrency = arrayListOf<Currency>().apply {
            CurrencyCode.values().forEach { currencyID ->
                val currency = java.util.Currency.getInstance(currencyID.name)
                add(
                    Currency(
                        name = "",
                        country = currency.displayName,
                        countryCode = currency.currencyCode,
                        symbol = currency.symbol
                    )
                )
            }
        }.sortedBy { it.country }
    }
}
