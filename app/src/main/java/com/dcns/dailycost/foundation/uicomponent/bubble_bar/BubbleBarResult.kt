package com.dcns.dailycost.foundation.uicomponent.bubble_bar

enum class BubbleBarResult {
	/**
	 * [Snackbar] that is shown has been dismissed either by timeout of by user
	 */
	Dismissed,

	/**
	 * Action on the [Snackbar] has been clicked before the time out passed
	 */
	ActionPerformed
}