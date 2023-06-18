package com.dcns.dailycost.data.serializer

import androidx.datastore.core.Serializer
import com.dcns.dailycost.ProtoUserCredential
import com.dcns.dailycost.foundation.common.EncryptionManager
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream

class UserCredentialSerializer(
    private val encryptionManager: EncryptionManager
): Serializer<ProtoUserCredential> {

    val s = object : KSerializer<ProtoUserCredential> {
        override fun deserialize(decoder: Decoder): ProtoUserCredential {
            return decoder.decodeStructure(descriptor) {
                var id = ""
                var name = ""
                var email = ""
                var token = ""
                var password = ""

                loop@ while (true) {
                    when (val index = decodeElementIndex(descriptor)) {
                        DECODE_DONE -> break@loop

                        0 -> id = decodeStringElement(descriptor, 0)
                        1 -> name = decodeStringElement(descriptor, 1)
                        2 -> email = decodeStringElement(descriptor, 2)
                        3 -> token = decodeStringElement(descriptor, 3)
                        4 -> password = decodeStringElement(descriptor, 4)

                        else -> throw SerializationException("Unexpected index $index")
                    }
                }

                ProtoUserCredential(id, name, email, token, password)
            }
        }

        override val descriptor: SerialDescriptor
            get() = buildClassSerialDescriptor("ProtoUserCredential") {
                element<String>("id")
                element<String>("name")
                element<String>("email")
                element<String>("token")
                element<String>("password")
            }

        override fun serialize(encoder: Encoder, value: ProtoUserCredential) {
            encoder.encodeStructure(descriptor) {
                encodeStringElement(descriptor, 0, value.id)
                encodeStringElement(descriptor, 1, value.name)
                encodeStringElement(descriptor, 2, value.email)
                encodeStringElement(descriptor, 3, value.token)
                encodeStringElement(descriptor, 4, value.password)
            }
        }
    }

    override val defaultValue: ProtoUserCredential
        get() = ProtoUserCredential()

    override suspend fun readFrom(input: InputStream): ProtoUserCredential {
        return try {
            val decryptedBytes = encryptionManager.decrypt(input)

            Json.decodeFromString(
                deserializer = s,
                string = decryptedBytes.decodeToString()
            )
        } catch (e: Exception) {
            Timber.e(e, "Cannot read proto")
            defaultValue
        }
    }

    override suspend fun writeTo(t: ProtoUserCredential, output: OutputStream) {
        encryptionManager.encrypt(
            bytes = Json.encodeToString(
                serializer = s,
                value = t
            ).encodeToByteArray(),
            outputStream = output
        )
    }
}