package com.dcns.dailycost.ui.top_up

import com.dcns.dailycost.ProtoUserBalance
import com.dcns.dailycost.ProtoUserCredential
import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.model.UserBalance
import com.dcns.dailycost.data.model.UserCredential
import com.dcns.dailycost.foundation.extension.toUserBalance
import com.dcns.dailycost.foundation.extension.toUserCredential

data class TopUpState(
    val balance: UserBalance = ProtoUserBalance().toUserBalance(),
    val credential: UserCredential = ProtoUserCredential().toUserCredential(),
    val selectedWalletType: WalletType = WalletType.Cash,
    val internetConnectionAvailable: Boolean = true,
    val amount: Double = 0.0
)
