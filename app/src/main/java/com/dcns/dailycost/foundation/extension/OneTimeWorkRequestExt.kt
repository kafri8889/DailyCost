package com.dcns.dailycost.foundation.extension

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

fun OneTimeWorkRequest.enqueue(context: Context) {
    WorkManager.getInstance(context).enqueue(this)
}
