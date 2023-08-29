package com.dcns.dailycost.foundation.base

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarDuration
import com.dcns.dailycost.foundation.uicomponent.bubble_bar.BubbleBarDuration

/**
 * UI event, digunakan ketika ingin menampilkan snackbar, toast, dll
 *
 * @author kafri8889
 */
open class UiEvent {

	object DismissCurrentSnackbar: UiEvent()

	open class ShowToast(
		open val message: String,
		open val length: Int = Toast.LENGTH_SHORT
	): UiEvent()

	open class ShowSnackbar(
		open val message: String,
		open val actionLabel: String?,
		open val withDismissAction: Boolean,
		open val duration: SnackbarDuration,
		open val data: Any?
	): UiEvent()

	open class ShowBubbleBar(
		open val message: String,
		open val actionLabel: String?,
		open val withDismissAction: Boolean,
		open val duration: BubbleBarDuration,
		open val data: Any?
	): UiEvent()

	fun String.parse(context: Context): String {
		return if (contains(AS_STRING_RES_ID)) {
			val split = split('|')
			val resId = split[1].toInt()

			context.getString(resId)
		} else this
	}

	companion object {
		const val AS_STRING_RES_ID = "as_string_res_id"

		/**
		 * If you want to display message from string resource
		 */
		fun asStringResource(
			@StringRes resId: Int
		): String {
			return "$AS_STRING_RES_ID|$resId"
		}
	}

}