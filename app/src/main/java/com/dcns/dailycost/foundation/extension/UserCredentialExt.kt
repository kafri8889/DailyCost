package com.dcns.dailycost.foundation.extension

import com.dcns.dailycost.ProtoUserCredential
import com.dcns.dailycost.data.model.UserCredential

fun ProtoUserCredential.toUserCredential(): UserCredential {
    return UserCredential(
        name = name,
        email = email,
        password = password
    )
}
