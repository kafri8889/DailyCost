package com.anafthdev.datemodule.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@Parcelize
data class Week(
	/**
	 * [Calendar.MONTH]
	 */
	val month: Int,
	val year: Int,
	val firstDay: Long,
	val lastDay: Long,
	val firstDayOfMonth: Int,
	val lastDayOfMonth: Int,
): Parcelable
