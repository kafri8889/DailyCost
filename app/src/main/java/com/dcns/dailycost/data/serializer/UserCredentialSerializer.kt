package com.dcns.dailycost.data.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.dcns.dailycost.ProtoUserCredential
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object UserCredentialSerializer: Serializer<ProtoUserCredential> {

    override val defaultValue: ProtoUserCredential
        get() = ProtoUserCredential()

    override suspend fun readFrom(input: InputStream): ProtoUserCredential {
        try {
            return ProtoUserCredential.ADAPTER.decode(input)
        } catch (e: IOException) {
            throw CorruptionException("Cannot read proto", e)
        }
    }

    override suspend fun writeTo(t: ProtoUserCredential, output: OutputStream) {
        ProtoUserCredential.ADAPTER.encode(output, t)
    }
}