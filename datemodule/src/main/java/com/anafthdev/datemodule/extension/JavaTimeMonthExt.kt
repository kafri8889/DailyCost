package com.anafthdev.datemodule.extension

import com.anafthdev.datemodule.DateModule
import java.time.Month
import java.util.Calendar

/**
 * Convert [java.time.Month] to [Calendar.MONTH]
 *
 * @return [Calendar.MONTH]
 */
fun Month.toCalendarMonth(): Int {
    return when(this) {
        Month.JANUARY -> Calendar.JANUARY
        Month.FEBRUARY -> Calendar.FEBRUARY
        Month.MARCH -> Calendar.MARCH
        Month.APRIL -> Calendar.APRIL
        Month.MAY -> Calendar.MAY
        Month.JUNE -> Calendar.JUNE
        Month.JULY -> Calendar.JULY
        Month.AUGUST -> Calendar.AUGUST
        Month.SEPTEMBER -> Calendar.SEPTEMBER
        Month.OCTOBER -> Calendar.OCTOBER
        Month.NOVEMBER -> Calendar.NOVEMBER
        Month.DECEMBER -> Calendar.DECEMBER
    }
}

/**
 * Get next month
 *
 * if current month is [Month.DECEMBER], next month will be [Month.JANUARY]
 */
fun Month.next(): Month {
    return DateModule.nextMonthOf(this)
}

/**
 * Get previous month
 *
 * if current month is [Month.JANUARY], previous month will be [Month.DECEMBER]
 */
fun Month.previous(): Month {
    return DateModule.previousMonthOf(this)
}

/**
 * Get next month
 *
 * if current month is [Month.DECEMBER], next month will be `null`
 */
fun Month.nextOrNull(): Month? {
    return DateModule.nextMonthOrNull(this)
}

/**
 * Get previous month
 *
 * if current month is [Month.JANUARY], previous month will be `null`
 */
fun Month.previousOrNull(): Month? {
    return DateModule.previousMonthOrNull(this)
}
