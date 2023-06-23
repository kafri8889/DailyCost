package com.dcns.dailycost.data.model

import android.os.Parcelable
import com.dcns.dailycost.data.Language
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class UserPreference(
    val language: @RawValue Language,
    val secureApp: Boolean
): Parcelable
