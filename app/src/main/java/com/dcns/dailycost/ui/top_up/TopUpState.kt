package com.dcns.dailycost.ui.top_up

import com.dcns.dailycost.ProtoUserBalance
import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.model.UserBalance
import com.dcns.dailycost.foundation.extension.toUserBalance

data class TopUpState(
    val balance: UserBalance = ProtoUserBalance().toUserBalance(),
    val selectedWalletType: WalletType = WalletType.Cash,
    val amount: Double = 0.0
)
