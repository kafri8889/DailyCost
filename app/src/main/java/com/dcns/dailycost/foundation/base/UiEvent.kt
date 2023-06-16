package com.dcns.dailycost.foundation.base

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarDuration

interface UiEvent {
	
	open class ShowSnackbar(
		open val message: String,
		open val actionLabel: String?,
		open val withDismissAction: Boolean,
		open val duration: SnackbarDuration,
		open val data: Any?,
	): UiEvent {
		
		fun getMessage(context: Context): String {
			return if (message.contains(AS_STRING_RES_ID)) {
				val split = message.split('|')
				val resId = split[1].toInt()
				
				context.getString(resId)
			} else message
		}
		
		fun getActionLabel(context: Context): String? {
			if (actionLabel == null) return null
			
			return if (actionLabel!!.contains(AS_STRING_RES_ID)) {
				val split = actionLabel!!.split('|')
				val resId = split[1].toInt()
				
				context.getString(resId)
			} else actionLabel
		}
		
		companion object {
			private const val AS_STRING_RES_ID = "as_string_res_id"
			
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
	
}