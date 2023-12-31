package com.dcns.dailycost.foundation.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.dcns.dailycost.data.model.remote.request_body.expense.AddExpenseRequestBody
import com.dcns.dailycost.data.model.remote.response.ErrorResponse
import com.dcns.dailycost.domain.use_case.ExpenseUseCases
import com.dcns.dailycost.domain.use_case.UserCredentialUseCases
import com.dcns.dailycost.foundation.extension.fromJson
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.RequestBody
import timber.log.Timber

@HiltWorker
class PostExpenseWorker @AssistedInject constructor(
	@Assisted private val context: Context,
	@Assisted params: WorkerParameters,
	private val expenseUseCases: ExpenseUseCases,
	private val userCredentialUseCases: UserCredentialUseCases
): CoroutineWorker(context, params) {

	override suspend fun doWork(): Result {
		val requestBody = inputData.getString(Workers.ARG_DATA_REQUEST_BODY)

		requestBody?.let { json ->
			val body = json.fromJson(AddExpenseRequestBody::class.java)

			return postExpense(body.toRequestBody())
		}

		return Result.failure(
			workDataOf(
				Workers.ARG_WORKER_MESSAGE_KEY to "Request body not found, please insert request body into worker data"
			)
		)
	}

	private suspend fun postExpense(requestBody: RequestBody): Result {
		val credential = userCredentialUseCases.getUserCredentialUseCase().firstOrNull()

		if (credential != null) {
			expenseUseCases.addRemoteExpenseUseCase(
				credential.id.toInt(),
				requestBody,
				credential.getAuthToken()
			).let {
				if (it.isSuccessful) {
					Timber.i("post finished")

					return Result.success(
						workDataOf(
							Workers.ARG_WORKER_IS_SUCCESS_KEY to true
						)
					)
				} else return try {
					val errorResponse =
						Gson().fromJson(it.errorBody()?.charStream(), ErrorResponse::class.java)

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