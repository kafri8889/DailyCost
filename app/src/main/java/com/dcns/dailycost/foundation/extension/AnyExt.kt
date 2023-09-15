package com.dcns.dailycost.foundation.extension

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.gson.Gson

fun Any?.toast(context: Context, length: Int = Toast.LENGTH_SHORT) {
	Handler(Looper.getMainLooper()).post {
		Toast.makeText(context, toString(), length).show()
	}
}

fun Any.toJson(): String = Gson().toJson(this)
