package com.dcns.dailycost.foundation.extension

import com.dcns.dailycost.ProtoUserPreference
import com.dcns.dailycost.data.Language
import com.dcns.dailycost.data.model.UserPreference

fun ProtoUserPreference.toUserPreference(): UserPreference {
	return UserPreference(
		language = Language.values()[language],
		secureApp = secureApp,
		isNotFirstInstall = isNotFirstInstall
	)
}
