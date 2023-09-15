package com.dcns.dailycost.foundation.worker

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.workDataOf
import com.dcns.dailycost.data.model.remote.request_body.DepoRequestBody
import com.dcns.dailycost.data.model.remote.request_body.expense.AddExpenseRequestBody
import com.dcns.dailycost.data.model.remote.request_body.expense.DeleteExpenseRequestBody
import com.dcns.dailycost.data.model.remote.request_body.income.AddIncomeRequestBody
import com.dcns.dailycost.data.model.remote.request_body.income.DeleteIncomeRequestBody
import com.dcns.dailycost.foundation.extension.toJson

object Workers {

	/**
	 * Key untuk cek apakah sukses atau tidak
	 */
	const val ARG_WORKER_IS_SUCCESS_KEY = "worker_is_success"

	/**
	 * Key untuk message
	 */
	const val ARG_WORKER_MESSAGE_KEY = "worker_message"

	/**
	 * Key untuk request body
	 */
	const val ARG_DATA_REQUEST_BODY = "data_request_body"

	const val TAG_POST_INCOME_WORKER = "worker_tag_post_income"
	const val TAG_POST_EXPENSE_WORKER = "worker_tag_post_expense"
	const val TAG_DELETE_INCOME_WORKER = "worker_tag_delete_income"
	const val TAG_DELETE_EXPENSE_WORKER = "worker_tag_delete_expense"
	const val TAG_EDIT_BALANCE_WORKER = "worker_tag_edit_balance"
	const val TAG_SYNC_WORKER = "worker_tag_sync_balance"

	fun postIncomeWorker(body: AddIncomeRequestBody): OneTimeWorkRequest {
		return OneTimeWorkRequestBuilder<PostIncomeWorker>()
			.setConstraints(
				Constraints(
					requiredNetworkType = NetworkType.CONNECTED
				)
			)
			.setInputData(
				workDataOf(
					ARG_DATA_REQUEST_BODY to body.toJson()
				)
			)
			.addTag(TAG_POST_INCOME_WORKER)
			.build()
	}

	fun postExpenseWorker(body: AddExpenseRequestBody): OneTimeWorkRequest {
		return OneTimeWorkRequestBuilder<PostExpenseWorker>()
			.setConstraints(
				Constraints(
					requiredNetworkType = NetworkType.CONNECTED
				)
			)
			.setInputData(
				workDataOf(
					ARG_DATA_REQUEST_BODY to body.toJson()
				)
			)
			.addTag(TAG_POST_EXPENSE_WORKER)
			.build()
	}

	fun deleteExpenseWorker(body: DeleteExpenseRequestBody): OneTimeWorkRequest {
		return OneTimeWorkRequestBuilder<DeleteExpenseWorker>()
			.setConstraints(
				Constraints(
					requiredNetworkType = NetworkType.CONNECTED
				)
			)
			.setInputData(
				workDataOf(
					ARG_DATA_REQUEST_BODY to body.toJson()
				)
			)
			.addTag(TAG_DELETE_EXPENSE_WORKER)
			.build()
	}

	fun deleteIncomeWorker(body: DeleteIncomeRequestBody): OneTimeWorkRequest {
		return OneTimeWorkRequestBuilder<DeleteIncomeWorker>()
			.setConstraints(
				Constraints(
					requiredNetworkType = NetworkType.CONNECTED
				)
			)
			.setInputData(
				workDataOf(
					ARG_DATA_REQUEST_BODY to body.toJson()
				)
			)
			.addTag(TAG_DELETE_INCOME_WORKER)
			.build()
	}

	fun editBalanceWorker(body: DepoRequestBody): OneTimeWorkRequest {
		return OneTimeWorkRequestBuilder<EditBalanceWorker>()
			.setConstraints(
				Constraints(
					requiredNetworkType = NetworkType.CONNECTED
				)
			)
			.setInputData(
				workDataOf(
					ARG_DATA_REQUEST_BODY to body.toJson()
				)
			)
			.addTag(TAG_EDIT_BALANCE_WORKER)
			.build()
	}

	fun syncWorker(): OneTimeWorkRequest {
		return OneTimeWorkRequestBuilder<SyncWorker>()
			.setConstraints(
				Constraints(
					requiredNetworkType = NetworkType.CONNECTED
				)
			)
			.addTag(TAG_SYNC_WORKER)
			.build()
	}

}