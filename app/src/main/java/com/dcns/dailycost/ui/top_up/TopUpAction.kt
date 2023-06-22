package com.dcns.dailycost.ui.top_up

import android.content.Context
import com.dcns.dailycost.data.WalletType

sealed interface TopUpAction {

    data class ChangeWalletBalance(val amount: Double): TopUpAction

    data class ChangeSelectedWalletType(val type: WalletType): TopUpAction

    data class TopUp(val context: Context): TopUpAction

}