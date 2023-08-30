package com.anafthdev.datemodule

import com.anafthdev.datemodule.extension.getWeek
import com.anafthdev.datemodule.extension.isToday
import com.anafthdev.datemodule.extension.isTomorrow
import com.anafthdev.datemodule.extension.isYesterday
import com.anafthdev.datemodule.extension.next
import com.anafthdev.datemodule.extension.nextOrNull
import com.anafthdev.datemodule.extension.previous
import com.anafthdev.datemodule.extension.previousOrNull
import com.anafthdev.datemodule.extension.toCalendarMonth
import com.anafthdev.datemodule.extension.tomorrow
import com.anafthdev.datemodule.extension.yesterday
import com.anafthdev.datemodule.infix_function.inSameDay
import com.anafthdev.datemodule.infix_function.inSameMonth
import com.anafthdev.datemodule.infix_function.inSameYear
import java.time.Month
import java.util.Calendar

// Tested on July 24, 2023

fun main() {

	println(DateModule.yesterday()) // 23-07-2023
	println(DateModule.tomorrow()) // 25-07-2023


	// 24-07-2023 08:00
	println(DateModule.yesterday(1690160400000)) // 23-07-2023 08:00
	println(DateModule.tomorrow(1690160400000)) // 25-07-2023 08:00
	println(1690160400000.yesterday()) // 23-07-2023 08:00
	println(1690160400000.tomorrow()) // 25-07-2023 08:00


	// 24-07-2023 08:00
	println(System.currentTimeMillis().isToday()) // true
	// 23-07-2023 07:00:00 GMT+7
	println(1690070400000.isYesterday()) // true
	// 25-07-2023 05:30 GMT+7
	println(1690237800000.isTomorrow()) // true


	// 24-07-2023 08:00 inSameDay 24-07-2023 17:00 GMT+7
	println(1690160400000 inSameDay 1690192800000) // true
	println(DateModule.inSameDay(1690160400000, 1690192800000)) // true

	// 24-07-2023 08:00 inSameDay 25-07-2023 05:30 GMT+7
	println(DateModule.inSameDay(1690160400000, 1690237800000)) // false


	// 24-07-2023 00:00 inSameMonth 30-07-2023 07:00:00 GMT+7
	println(1690156800000 inSameMonth 1690675200000) // true
	println(DateModule.inSameMonth(1690156800000, 1690675200000)) // true

	// // 24-07-2023 00:00 inSameMonth 30-08-2023 07:00:00 GMT+7
	println(DateModule.inSameMonth(1690156800000, 1693353600000)) // false


	// 24-07-2023 00:00 inSameYear 30-07-2023 07:00:00 GMT+7
	println(1690156800000 inSameYear 1690675200000) // true
	println(DateModule.inSameYear(1690156800000, 1690675200000)) // true

	// // 24-07-2023 00:00 inSameYear 30-08-2023 07:00:00 GMT+7
	println(DateModule.inSameYear(1690156800000, 1722297600000)) // false


	// Convert java.util.calendar Month to java.time.Month
	println(DateModule.calendarMonthToJavaTimeMonth(Calendar.AUGUST)!!) // AUGUST


	// Convert java.time.Month to java.util.calendar Month
	println(DateModule.javaTimeMonthToCalendarMonth(Month.AUGUST)) // 7
	println(Month.AUGUST.toCalendarMonth()) // 7


	// Get next month of java.time.Month
	println(DateModule.nextMonthOf(Month.AUGUST)) // SEPTEMBER
	println(DateModule.nextMonthOf(Month.DECEMBER)) // JANUARY
	println(Month.AUGUST.next()) // SEPTEMBER
	println(Month.DECEMBER.next()) // JANUARY
	// or null
	println(DateModule.nextMonthOrNull(Month.AUGUST)) // SEPTEMBER
	println(DateModule.nextMonthOrNull(Month.DECEMBER)) // null
	println(Month.AUGUST.nextOrNull()) // SEPTEMBER
	println(Month.DECEMBER.nextOrNull()) // null


	// Get previous month of java.time.Month
	println(DateModule.previousMonthOf(Month.AUGUST)) // JULY
	println(DateModule.previousMonthOf(Month.JANUARY)) // DECEMBER
	println(Month.AUGUST.previous()) // JULY
	println(Month.JANUARY.previous()) // DECEMBER
	// or null
	println(DateModule.previousMonthOrNull(Month.AUGUST)) // JULY
	println(DateModule.previousMonthOrNull(Month.JANUARY)) // null
	println(Month.AUGUST.previousOrNull()) // JULY
	println(Month.JANUARY.previousOrNull()) // null


	// Indonesian first day of week: Monday
	// 24-07-2023 07:00:00 GMT+7
	println(DateModule.isFirstDayOfWeek(1690156800000)) // true


	// 24-07-2023
	DateModule.getWeek(1690166772000L).let {
		println("${it.firstDayOfMonth} - ${it.lastDayOfMonth}") // 24 - 30
	}


	// 24-07-2023 07:00:00 GMT+7 until 31-07-2023 07:00:00 GMT+7
	val availableWeeks = DateModule.getAvailableWeeks(1690156800000, 1690761600000)
	println(availableWeeks)
	// [Week(month=6, year=2023, firstDay=1690131601000, lastDay=1690736399000, firstDayOfMonth=24, lastDayOfMonth=30),
	// Week(month=7, year=2023, firstDay=1690736401000, lastDay=1691341199000, firstDayOfMonth=31, lastDayOfMonth=6)]


	// Get: 27-07-2023 07:00:00 GMT+7
	val week1 = DateModule.getWeekInWeeks(availableWeeks, 1690416000000)!!
	// Get: 04-08-2023 07:00:00 GMT+7
	val week2 = availableWeeks.getWeek(1691107200000)!!
	// Get: 10-08-2023 07:00:00 GMT+7
	val weekNull = availableWeeks.getWeek(1691625600000) // Out of range (Jul 24 until Aug 6)
	println(week1) // Week(month=6, year=2023, firstDay=1690131601000, lastDay=1690736399000, firstDayOfMonth=24, lastDayOfMonth=30)
	println(week2) // Week(month=7, year=2023, firstDay=1690736401000, lastDay=1691341199000, firstDayOfMonth=31, lastDayOfMonth=6)
	println(weekNull) // null


	// 24-07-2023 07:00:00 GMT+7
	val day = DateRange.day(1690156800000)
	val week = DateRange.week(1690156800000)
	val month = DateRange.month(1690156800000)
	val year = DateRange.year(1690156800000)
	// 24-07-2023 07:00:00 GMT+7 until 04-08-2023 07:00:00 GMT+7
	val custom = DateRange.of(1690156800000, 1691107200000)
	println(day) // {"start":1690131601000,"end":1690217999000} | 24-07-2023 00:00:01 GMT+7 until 24-07-2023 23:59:29 GMT+7
	println(week) // {"start":1690131600000,"end":1690736399000} | 24-07-2023 00:00:00 GMT+7 until 30-07-2023 23:59:29 GMT+7
	println(month) // {"start":1688144401000,"end":1690822799000} | 01-07-2023 00:00:01 GMT+7 until 31-07-2023 23:59:29 GMT+7
	println(year) // {"start":1672506001000,"end":1704041999000} | 01-01-2023 00:00:01 GMT+7 until 31-12-2023 23:59:29 GMT+7


}
