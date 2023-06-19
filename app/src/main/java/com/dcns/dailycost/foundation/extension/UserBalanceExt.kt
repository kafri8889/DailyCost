package com.dcns.dailycost.foundation.extension

import com.dcns.dailycost.ProtoUserBalance
import com.dcns.dailycost.data.model.UserBalance

fun ProtoUserBalance.toUserBalance(): UserBalance {
    return UserBalance(cash, eWallet, bankAccount)
}
