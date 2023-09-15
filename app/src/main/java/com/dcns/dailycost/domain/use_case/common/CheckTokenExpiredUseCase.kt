package com.dcns.dailycost.domain.use_case.common

import com.dcns.dailycost.domain.repository.IBalanceRepository
import com.dcns.dailycost.domain.repository.IUserCredentialRepository
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import java.net.SocketTimeoutException

/**
 * Use case yang digunakan untuk mengecek apakah token sudah kadaluarsa atau belum
 * Jika token sudah kadaluarsa, user harus login ulang
 *
 * Untuk pengecekan, akan menggunakan getRemoteBalance
 */
class CheckTokenExpiredUseCase(
	private val credentialRepository: IUserCredentialRepository,
	private val balanceRepository: IBalanceRepository
) {

	/**
	 * @return true if the token is expired, false otherwise
	 */
	suspend operator fun invoke(): Boolean {
		try {
			credentialRepository.getUserCredential.firstOrNull()?.let { cred ->
				balanceRepository.getRemoteBalance(cred.id.toInt(), cred.getAuthToken()).let { response ->
					return !response.isSuccessful && cred.allNotEmpty
				}
			}
		} catch (e: SocketTimeoutException) {
			Timber.e(e, "Socket time out")
		} catch (e: Exception) {
			Timber.e(e)
		}

		return false
	}

}