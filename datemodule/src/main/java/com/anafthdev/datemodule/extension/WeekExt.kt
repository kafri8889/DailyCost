package com.anafthdev.datemodule.extension

import com.anafthdev.datemodule.model.Week

/**
 * Get week from week collection with timeInMillis
 */
fun Collection<Week>.getWeek(timeInMillis: Long): Week? {
    forEach { if (timeInMillis in it.firstDay..it.lastDay) return it }
    return null
}
