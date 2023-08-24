package com.dcns.dailycost.data.serializer

import androidx.datastore.core.Serializer
import com.dcns.dailycost.ProtoUserBalance
import java.io.InputStream
import java.io.OutputStream

object UserBalanceSerializer: Serializer<ProtoUserBalance> {

	override val defaultValue: ProtoUserBalance
		get() = ProtoUserBalance()

	override suspend fun readFrom(input: InputStream): ProtoUserBalance {
		return ProtoUserBalance.ADAPTER.decode(input)
	}

	override suspend fun writeTo(t: ProtoUserBalance, output: OutputStream) {
		ProtoUserBalance.ADAPTER.encode(output, t)
	}
}