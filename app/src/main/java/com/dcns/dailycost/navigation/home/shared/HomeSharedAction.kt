package com.dcns.dailycost.navigation.home.shared

sealed interface HomeSharedAction {

	data class UpdateSelectedArgbColor(val argb: Int): HomeSharedAction

}