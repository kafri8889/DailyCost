package com.anafthdev.datemodule.infix_function

import com.anafthdev.datemodule.DateModule

/**
 * Check if two date in the same day
 *
 * @param other other date (time in milliseconds)
 */
infix fun Long.inSameDay(other: Long): Boolean {
    return DateModule.inSameDay(this, other)
}

/**
 * Check if two date in the same month
 *
 * @param other other date (time in milliseconds)
 */
infix fun Long.inSameMonth(other: Long): Boolean {
    return DateModule.inSameMonth(this, other)
}

/**
 * Check if two date in the same year
 *
 * @param other other date (time in milliseconds)
 */
infix fun Long.inSameYear(other: Long): Boolean {
    return DateModule.inSameYear(this, other)
}
