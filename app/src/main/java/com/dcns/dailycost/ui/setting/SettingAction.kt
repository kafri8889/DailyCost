package com.dcns.dailycost.ui.setting

import android.content.Context

sealed interface SettingAction {

    data class Logout(val context: Context): SettingAction

}