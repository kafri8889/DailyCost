package com.dcns.dailycost.foundation.extension

import com.google.gson.Gson

fun <T> String.fromJson(klass: Class<T>): T {
    return Gson().fromJson(this, klass)
}
