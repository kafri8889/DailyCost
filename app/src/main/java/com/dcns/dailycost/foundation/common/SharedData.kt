package com.dcns.dailycost.foundation.common

import com.dcns.dailycost.data.model.Category
import com.dcns.dailycost.data.model.Wallet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class SharedData @Inject constructor() {

	private val _category = MutableStateFlow<Category?>(null)
	val category: StateFlow<Category?> = _category

	private val _wallet = MutableStateFlow<Wallet?>(null)
	val wallet: StateFlow<Wallet?> = _wallet

	fun setCategory(category: Category?) {
		_category.update { category }
	}

	fun setWallet(wallet: Wallet?) {
		_wallet.update { wallet }
	}

}