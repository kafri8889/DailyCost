package com.dcns.dailycost.foundation.extension

import android.content.Context
import java.util.Locale

val Context.primaryLocale: Locale
    get() = resources.configuration.locales[0]
