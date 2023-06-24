package com.dcns.dailycost.ui.app

import com.dcns.dailycost.ProtoUserBalance
import com.dcns.dailycost.data.model.UserBalance
import com.dcns.dailycost.data.model.UserCredential
import com.dcns.dailycost.foundation.extension.toUserBalance

data class DailyCostAppState(
    val userBalance: UserBalance = ProtoUserBalance().toUserBalance(),
    val userCredential: UserCredential? = null,
    val currentDestinationRoute: String = "",
    /**
     * if secure app with biometric enabled
     */
    val isSecureAppEnabled: Boolean = false,
    val isBiometricAuthenticated: Boolean = false
)
