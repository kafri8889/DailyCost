package com.dcns.dailycost.ui.transaction

import android.widget.Toast
import androidx.compose.material3.SnackbarDuration
import com.dcns.dailycost.R
import com.dcns.dailycost.foundation.base.UiEvent

sealed class TransactionUiEvent: UiEvent() {

    class NoInternetConnection(
        override val message: String = asStringResource(R.string.internet_connection_not_available),
        override val actionLabel: String? = null,
        override val withDismissAction: Boolean = true,
        override val duration: SnackbarDuration = SnackbarDuration.Short,
        override val data: Any? = null,
    ): ShowSnackbar(message, actionLabel, withDismissAction, duration, data)

    class FailedToDelete(
        override val message: String = asStringResource(R.string.failed_to_delete),
        override val actionLabel: String? = null,
        override val withDismissAction: Boolean = true,
        override val duration: SnackbarDuration = SnackbarDuration.Short,
        override val data: Any? = null,
    ): ShowSnackbar(message, actionLabel, withDismissAction, duration, data)

    class Deleting(
        override val message: String = asStringResource(R.string.deleteing),
        override val actionLabel: String? = null,
        override val withDismissAction: Boolean = true,
        override val duration: SnackbarDuration = SnackbarDuration.Indefinite,
        override val data: Any? = null,
    ): ShowSnackbar(message, actionLabel, withDismissAction, duration, data)

    class TransactionDeleted(
        override val message: String = asStringResource(R.string.transaction_deleted),
        override val length: Int = Toast.LENGTH_SHORT
    ): ShowToast(message, length)

}