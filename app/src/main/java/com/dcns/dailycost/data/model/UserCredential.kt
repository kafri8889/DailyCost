package com.dcns.dailycost.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Class ini digunakan untuk menyimpan informasi login pengguna
 */
@Parcelize
data class UserCredential(
    val id: Int,
    val name: String,
    val email: String,
    val token: String,
    val password: String
): Parcelable {

    val isLoggedIn: Boolean
        get() = name.isNotBlank() &&
                email.isNotBlank() &&
                token.isNotBlank() &&
                password.isNotBlank()

}
