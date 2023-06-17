package com.dcns.dailycost.foundation.extension

import com.dcns.dailycost.ProtoUserCredential
import com.dcns.dailycost.data.model.UserCredential

fun ProtoUserCredential.toUserCredential(): UserCredential {
    return UserCredential(
        id = id,
        name = name,
        email = email,
        token = token,
        password = password
    )
}
