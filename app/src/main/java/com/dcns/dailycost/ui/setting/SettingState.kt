package com.dcns.dailycost.ui.setting

import com.dcns.dailycost.ProtoUserCredential
import com.dcns.dailycost.data.Language
import com.dcns.dailycost.data.model.UserCredential
import com.dcns.dailycost.foundation.extension.toUserCredential

data class SettingState(
    val userCredential: UserCredential = ProtoUserCredential().toUserCredential(),
    val language: Language = Language.English
)
