package com.dcns.dailycost.ui.top_up

import com.dcns.dailycost.data.WalletType

sealed interface TopUpAction {

    data class ChangeWalletBalance(val amount: Double): TopUpAction

    data class ChangeSelectedWalletType(val type: WalletType): TopUpAction

    object TopUp: TopUpAction

}