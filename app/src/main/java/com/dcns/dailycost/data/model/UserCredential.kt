package com.dcns.dailycost.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserCredential(
    val name: String,
    val email: String,
    val password: String
): Parcelable {

    val isLoggedIn: Boolean
        get() = name.isNotBlank() and email.isNotBlank() and password.isNotBlank()

}
