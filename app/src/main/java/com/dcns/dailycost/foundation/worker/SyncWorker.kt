package com.dcns.dailycost.foundation.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.dcns.dailycost.data.model.UserCredential
import com.dcns.dailycost.data.model.remote.response.ErrorResponse
import com.dcns.dailycost.domain.repository.IUserCredentialRepository
import com.dcns.dailycost.domain.use_case.BalanceUseCases
import com.dcns.dailycost.domain.use_case.NoteUseCases
import com.dcns.dailycost.domain.use_case.ShoppingUseCases
import com.dcns.dailycost.domain.util.GetNoteBy
import com.dcns.dailycost.foundation.common.Workers
import com.dcns.dailycost.foundation.extension.toNote
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Sync all data from server
 */
@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val noteUseCases: NoteUseCases,
    private val balanceUseCases: BalanceUseCases,
    private val shoppingUseCases: ShoppingUseCases,
    private val userCredentialRepository: IUserCredentialRepository
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val credential = userCredentialRepository.getUserCredential.firstOrNull()
            ?: return Result.failure(
                workDataOf(
                    Workers.ARG_WORKER_MESSAGE_KEY to "Null credential"
                )
            )

        val results = listOf(
            getBalance(credential),
            getNote(credential)
        )

        return if (results.all { it is Result.Success }) Result.success()
        else Result.retry()
    }

    private suspend fun getNote(credential: UserCredential): Result {
        noteUseCases.getRemoteNoteUseCase(
            token = credential.getAuthToken(),
            getNoteBy = GetNoteBy.UserID(credential.id.toInt())
        ).let { response ->
            if (response.isSuccessful) {
                val noteResponse = response.body()

                noteResponse?.let {
                    Timber.i("upserting notes to db...")
                    withContext(Dispatchers.IO) {
                        noteUseCases.upsertLocalNoteUseCase(
                            *noteResponse.data
                                .map { it.toNote() }
                                .toTypedArray()
                        )
                    }
                }

                Timber.i("get remote note success")

                return Result.success()
            }

            val errorResponse = Gson().fromJson(
                response.errorBody()?.charStream(),
                ErrorResponse::class.java
            )

            Timber.e("failed to get remote note: ${errorResponse.message}")

            return Result.failure(
                workDataOf(
                    Workers.ARG_WORKER_MESSAGE_KEY to errorResponse.message
                )
            )
        }
    }

    private suspend fun getBalance(credential: UserCredential): Result {
        balanceUseCases.getRemoteBalanceUseCase(
            token = credential.getAuthToken(),
            userId = credential.id.toInt()
        ).let { response ->
            if (response.isSuccessful) {
                val balanceResponseData = response.body()?.data

                balanceResponseData?.let {
                    balanceUseCases.updateLocalBalanceUseCase(
                        cash = balanceResponseData.cash.toDouble(),
                        eWallet = balanceResponseData.eWallet.toDouble(),
                        bankAccount = balanceResponseData.bankAccount.toDouble()
                    )
                }

                Timber.i("get remote balance success")

                return Result.success()
            }

            val errorResponse = Gson().fromJson(
                response.errorBody()?.charStream(),
                ErrorResponse::class.java
            )

            Timber.e("failed to get remote balance: ${errorResponse.message}")

            return Result.failure(
                workDataOf(
                    Workers.ARG_WORKER_MESSAGE_KEY to errorResponse.message
                )
            )
        }
    }

}