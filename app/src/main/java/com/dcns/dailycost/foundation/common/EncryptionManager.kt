package com.dcns.dailycost.foundation.common

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject

/**
 * Digunakan untuk mengenkripsi, salah satunya data store
 *
 * from https://www.youtube.com/watch?v=D_q07P1sfcc
 */
class EncryptionManager @Inject constructor() {

	private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
		load(null)
	}

	private val encryptCipher
		get() = Cipher.getInstance(TRANSFORMATION).apply {
			init(Cipher.ENCRYPT_MODE, getKey())
		}

	private fun getDecryptCipherForIv(iv: ByteArray): Cipher {
		return Cipher.getInstance(TRANSFORMATION).apply {
			init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
		}
	}

	private fun getKey(): SecretKey {
		val existingKey = keyStore.getEntry("secret", null) as? KeyStore.SecretKeyEntry
		return existingKey?.secretKey ?: createKey()
	}

	private fun createKey(): SecretKey {
		return KeyGenerator.getInstance(ALGORITHM).apply {
			init(
				KeyGenParameterSpec.Builder(
					"secret",
					KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
				)
					.setBlockModes(BLOCK_MODE)
					.setEncryptionPaddings(PADDING)
					.setUserAuthenticationRequired(false)
					.setRandomizedEncryptionRequired(true)
					.build()
			)
		}.generateKey()
	}

	fun encrypt(bytes: ByteArray, outputStream: OutputStream) {
		val cipher = encryptCipher
		val iv = cipher.iv
		outputStream.use {
			it.write(iv)
			// write the payload in chunks to make sure to support larger data amounts (this would otherwise fail silently and result in corrupted data being read back)
			////////////////////////////////////
			val inputStream = ByteArrayInputStream(bytes)
			val buffer = ByteArray(CHUNK_SIZE)
			while (inputStream.available() > CHUNK_SIZE) {
				inputStream.read(buffer)
				val ciphertextChunk = cipher.update(buffer)
				it.write(ciphertextChunk)
			}
			// the last chunk must be written using doFinal() because this takes the padding into account
			val remainingBytes = inputStream.readBytes()
			val lastChunk = cipher.doFinal(remainingBytes)
			it.write(lastChunk)
			//////////////////////////////////
		}
	}

	fun decrypt(inputStream: InputStream): ByteArray {
		return inputStream.use {
			val iv = ByteArray(KEY_SIZE)
			it.read(iv)
			val cipher = getDecryptCipherForIv(iv)
			val outputStream = ByteArrayOutputStream()

			// read the payload in chunks to make sure to support larger data amounts (this would otherwise fail silently and result in corrupted data being read back)
			////////////////////////////////////
			val buffer = ByteArray(CHUNK_SIZE)
			while (inputStream.available() > CHUNK_SIZE) {
				inputStream.read(buffer)
				val ciphertextChunk = cipher.update(buffer)
				outputStream.write(ciphertextChunk)
			}
			// the last chunk must be read using doFinal() because this takes the padding into account
			val remainingBytes = inputStream.readBytes()
			val lastChunk = cipher.doFinal(remainingBytes)
			outputStream.write(lastChunk)
			//////////////////////////////////

			outputStream.toByteArray()
		}
	}

	companion object {
		private const val CHUNK_SIZE = 1024 * 4 // bytes
		private const val KEY_SIZE = 16 // bytes
		private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
		private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
		private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
		private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
	}

}