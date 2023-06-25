package com.dcns.dailycost.foundation.common

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.workDataOf
import com.dcns.dailycost.data.model.remote.request_body.DepoRequestBody
import com.dcns.dailycost.data.model.remote.request_body.IncomeRequestBody
import com.dcns.dailycost.foundation.worker.EditBalanceWorker
import com.dcns.dailycost.foundation.worker.PostIncomeBalanceWorker
import com.dcns.dailycost.foundation.worker.SyncWorker

object Workers {

    const val ARG_WORKER_IS_SUCCESS_KEY = "worker_is_success"
    const val ARG_WORKER_MESSAGE_KEY = "worker_message"

    const val ARG_DATA_REQUEST_BODY = "data_request_body"

    const val TAG_POST_INCOME_BALANCE_WORKER = "worker_tag_post_income_balance"
    const val TAG_EDIT_BALANCE_WORKER = "worker_tag_edit_balance"
    const val TAG_SYNC_WORKER = "worker_tag_sync_balance"

    fun postIncomeWorker(body: IncomeRequestBody): OneTimeWorkRequest {
        return OneTimeWorkRequestBuilder<PostIncomeBalanceWorker>()
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
            .addTag(TAG_POST_INCOME_BALANCE_WORKER)
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