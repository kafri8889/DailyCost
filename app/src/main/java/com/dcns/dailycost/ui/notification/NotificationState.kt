package com.dcns.dailycost.ui.notification

import android.os.Parcelable
import com.dcns.dailycost.data.model.Notification
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationState(
	val notifications: List<Notification> = emptyList()
): Parcelable
