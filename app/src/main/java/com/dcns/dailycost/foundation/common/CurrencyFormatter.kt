package com.dcns.dailycost.foundation.common

import com.dcns.dailycost.data.CurrencyCode
import com.dcns.dailycost.data.Locales
import timber.log.Timber
import java.text.NumberFormat
import java.text.ParseException
import java.util.Currency
import java.util.Locale

object CurrencyFormatter {

    fun getSymbol(
        locale: Locale,
        currencyCode: String
    ): String {
        return NumberFormat.getCurrencyInstance(getLocale(currencyCode, locale)).apply{
            currency = Currency.getInstance(currencyCode)
        }.format(1.0).replace("[0-9.,]".toRegex(), "")
    }

    fun format(
        locale: Locale,
        amount: Double,
        useSymbol: Boolean = true,
        countryCode: String = ""
    ): String {
        var firstDigitIndex = -1
        val numberFormat = NumberFormat.getCurrencyInstance(getLocale(countryCode, locale)).apply {
            if (countryCode.isNotBlank()) {
                Timber.i("currrrrr: -> ${currency?.currencyCode}")
                currency = Currency.getInstance(countryCode)
                Timber.i("pinnnnnnnn: $countryCode -> ${currency!!.currencyCode}")
            }
        }

        val formattedAmount = numberFormat.format(amount)

        formattedAmount.forEachIndexed { i, c ->
            if (c.isDigit() && firstDigitIndex < 0) firstDigitIndex = i
        }

        return "${if (useSymbol) "${formattedAmount.substring(0, firstDigitIndex)} " else ""}${formattedAmount.subSequence(firstDigitIndex, formattedAmount.length)}"
    }

    fun parse(
        locale: Locale,
        amount: String,
        currencyCode: String = ""
    ): Double {
        val numberFormat = NumberFormat.getCurrencyInstance(getLocale(currencyCode, locale)).apply {
            if (currencyCode.isNotBlank()) {
                currency = Currency.getInstance(currencyCode)
            }
        }

        return try {
            numberFormat.parse(amount)?.toDouble() ?: 0.0
        } catch (e: ParseException) {
            0.0
        }
    }

    private fun getLocale(currencyCode: String, default: Locale): Locale {
        return when (currencyCode) {
            CurrencyCode.USD.name -> Locale.US
            CurrencyCode.KRW.name -> Locale.KOREA
            CurrencyCode.CAD.name -> Locale.CANADA
            CurrencyCode.CNY.name -> Locale.CHINA
            CurrencyCode.EUR.name -> Locale.FRANCE
            CurrencyCode.GBP.name -> Locale.UK
            CurrencyCode.JPY.name -> Locale.JAPAN
            CurrencyCode.TWD.name -> Locale.TAIWAN
            CurrencyCode.IDR.name -> Locales.INDONESIAN
            CurrencyCode.RUB.name -> Locales.RUSSIAN_RUSSIA
            CurrencyCode.INR.name -> Locales.HINDI_INDIAN
            CurrencyCode.BRL.name -> Locales.PORTUGUESE_BRAZIL
            CurrencyCode.DZD.name -> Locales.ARABIC_ALGERIAN
            CurrencyCode.BHD.name -> Locales.ARABIC_BAHRAIN
            CurrencyCode.IQD.name -> Locales.ARABIC_IRAQ
            CurrencyCode.JOD.name -> Locales.ARABIC_JORDANIAN
            CurrencyCode.KWD.name -> Locales.ARABIC_KUWAIT
            CurrencyCode.LYD.name -> Locales.ARABIC_LIBYA
            CurrencyCode.RSD.name -> Locales.ARABIC_SERBIAN
            CurrencyCode.TND.name -> Locales.ARABIC_TUNISIAN
            CurrencyCode.SAR.name -> Locales.ARABIC_SERBIAN
            CurrencyCode.AED.name -> Locales.ARABIC_UNITED_ARAB_EMIRATES
            else -> default
        }
    }

}