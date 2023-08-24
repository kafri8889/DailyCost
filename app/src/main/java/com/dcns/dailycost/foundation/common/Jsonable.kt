package com.dcns.dailycost.foundation.common

import com.google.gson.Gson

abstract class Jsonable {

	fun toJson(): String = Gson().toJson(this)

}