package com.dcns.dailycost.domain.util

sealed class EditUserPreferenceType {

    data class Language(val value: com.dcns.dailycost.data.Language): EditUserPreferenceType()
    data class SecureApp(val value: Boolean): EditUserPreferenceType()
    data class IsNotFirstInstall(val value: Boolean): EditUserPreferenceType()

}