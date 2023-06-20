package com.dcns.dailycost.foundation.common

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import com.dcns.dailycost.data.model.Currency

val LocalCurrency: ProvidableCompositionLocal<Currency> = compositionLocalOf { Currency.rupiah }
