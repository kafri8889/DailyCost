package com.dcns.dailycost.ui.dashboard

import com.dcns.dailycost.ProtoUserCredential
import com.dcns.dailycost.data.model.UserCredential
import com.dcns.dailycost.foundation.extension.toUserCredential

data class DashboardState(
    val credential: UserCredential = ProtoUserCredential().toUserCredential(),
    val isRefreshing: Boolean = false
)
