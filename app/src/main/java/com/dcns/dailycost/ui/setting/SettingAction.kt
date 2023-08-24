package com.dcns.dailycost.ui.setting

sealed interface SettingAction {

	data class UpdateIsSecureAppEnabled(val enabled: Boolean): SettingAction

}