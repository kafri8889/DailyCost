package com.dcns.dailycost.foundation.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.dcns.dailycost.data.model.remote.request_body.DepoRequestBody
import com.dcns.dailycost.data.model.remote.response.ErrorResponse
import com.dcns.dailycost.domain.repository.IUserCredentialRepository
import com.dcns.dailycost.domain.use_case.DepoUseCases
import com.dcns.dailycost.foundation.common.Workers
import com.dcns.dailycost.foundation.extension.fromJson
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.RequestBody

@HiltWorker
class UploadBalanceWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val depoUseCases: DepoUseCases,
    private val userCredentialRepository: IUserCredentialRepository
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val requestBody = inputData.getString(Workers.ARG_DATA_REQUEST_BODY)

        requestBody?.let { json ->
            val body = json.fromJson(DepoRequestBody::class.java)

            return uploadBalance(body.toRequestBody())
        }

        return Result.failure(
            workDataOf(
                Workers.ARG_WORKER_MESSAGE_KEY to "Request body not found, please insert request body into worker data"
            )
        )
    }

//    override suspend fun getForegroundInfo(): ForegroundInfo {
//
//        val notification = NotificationCompat.Builder(context, NotificationUtil.WORKER_CHANNEL_ID).apply {
//            setOngoing(true)
//            setLocalOnly(true)
//            setAutoCancel(true)
//            setOnlyAlertOnce(true)
//            setVisibility(NotificationCompat.VISIBILITY_SECRET)
//
//            priority = NotificationCompat.PRIORITY_MIN
//        }.build()
//    }

    private suspend fun uploadBalance(requestBody: RequestBody): Result {
        val token = userCredentialRepository.getUserCredential.firstOrNull()?.getAuthToken()

        if (token != null) {
            depoUseCases.editDepoUseCase(requestBody, token).let {
                if (it.isSuccessful) return Result.success()
                else return try {
                    val errorResponse = Gson().fromJson(it.errorBody()?.charStream(), ErrorResponse::class.java)

                    Result.failure(
                        workDataOf(
                            Workers.ARG_WORKER_MESSAGE_KEY to errorResponse.message
                        )
                    )
                } catch (e: Exception) {
                    e.printStackTrace()

                    Result.failure(
                        workDataOf(
                            Workers.ARG_WORKER_MESSAGE_KEY to "Nothing :("
                        )
                    )
                }
            }
        }

        return Result.failure(
            workDataOf(
                Workers.ARG_WORKER_MESSAGE_KEY to "Null token"
            )
        )
    }
}