package com.dcns.dailycost.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * Class ini digunakan untuk menyimpan informasi login pengguna
 */
@Parcelize
@Serializable
data class UserCredential(
    val id: String,
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

    /**
     * Get token for Authorization
     */
    fun getToken(): String {
        return "Bearer $token"
    }

}
