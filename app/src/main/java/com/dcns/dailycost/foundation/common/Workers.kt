package com.dcns.dailycost.foundation.common

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.workDataOf
import com.dcns.dailycost.data.model.remote.request_body.DepoRequestBody
import com.dcns.dailycost.foundation.worker.SyncWorker
import com.dcns.dailycost.foundation.worker.UploadBalanceWorker

object Workers {

    const val ARG_WORKER_IS_SUCCESS_KEY = "worker_is_success"
    const val ARG_WORKER_MESSAGE_KEY = "worker_message"

    const val ARG_DATA_REQUEST_BODY = "data_request_body"

    const val TAG_UPLOAD_WORKER = "worker_tag_upload_balance"
    const val TAG_SYNC_WORKER = "worker_tag_sync_balance"

    fun uploadBalanceWorker(body: DepoRequestBody): OneTimeWorkRequest {
        return OneTimeWorkRequestBuilder<UploadBalanceWorker>()
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
            .addTag(TAG_UPLOAD_WORKER)
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