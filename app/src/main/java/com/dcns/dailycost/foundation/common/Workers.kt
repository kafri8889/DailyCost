package com.dcns.dailycost.foundation.common

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.workDataOf
import com.dcns.dailycost.data.model.remote.request_body.DepoRequestBody
import com.dcns.dailycost.foundation.worker.UploadBalanceWorker

object Workers {

    const val ARG_WORKER_MESSAGE_KEY = "worker_message"

    const val ARG_DATA_REQUEST_BODY = "data_request_body"

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
            .build()
    }

}