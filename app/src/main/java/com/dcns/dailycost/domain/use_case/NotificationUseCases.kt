package com.dcns.dailycost.domain.use_case

import com.dcns.dailycost.domain.use_case.notification.GetLocalNotificationUseCase
import com.dcns.dailycost.domain.use_case.notification.InsertLocalNotificationUseCase
import com.dcns.dailycost.domain.use_case.notification.UpdateLocalNotificationUseCase

data class NotificationUseCases(
	val getLocalNotificationUseCase: GetLocalNotificationUseCase,
	val updateLocalNotificationUseCase: UpdateLocalNotificationUseCase,
	val insertLocalNotificationUseCase: InsertLocalNotificationUseCase
)
