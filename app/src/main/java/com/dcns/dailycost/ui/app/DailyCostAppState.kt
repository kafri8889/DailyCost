package com.dcns.dailycost.ui.app

import com.dcns.dailycost.data.model.UserCredential

data class DailyCostAppState(
    val userCredential: UserCredential? = null,
    val currentDestinationRoute: String = "",
)
