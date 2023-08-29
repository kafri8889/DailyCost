package com.dcns.dailycost.foundation.uicomponent.bubble_bar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.AccessibilityManager
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume

interface BubbleBarVisuals {
	val message: String
	val actionLabel: String?
	val withDismissAction: Boolean
	val duration: BubbleBarDuration
}

interface BubbleBarData {
	val visuals: BubbleBarVisuals

	/**
	 * Function to be called when BubbleBar action has been performed to notify the listeners.
	 */
	fun performAction()

	/**
	 * Function to be called when BubbleBar is dismissed either by timeout or by the user.
	 */
	fun dismiss()
}

private class BubbleBarVisualsImpl(
	override val message: String,
	override val actionLabel: String?,
	override val withDismissAction: Boolean,
	override val duration: BubbleBarDuration
): BubbleBarVisuals {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || this::class != other::class) return false

		other as BubbleBarVisualsImpl

		if (message != other.message) return false
		if (actionLabel != other.actionLabel) return false
		if (withDismissAction != other.withDismissAction) return false
		if (duration != other.duration) return false

		return true
	}

	override fun hashCode(): Int {
		var result = message.hashCode()
		result = 31 * result + actionLabel.hashCode()
		result = 31 * result + withDismissAction.hashCode()
		result = 31 * result + duration.hashCode()
		return result
	}
}

private class BubbleBarDataImpl(
	override val visuals: BubbleBarVisuals,
	private val continuation: CancellableContinuation<BubbleBarResult>
): BubbleBarData {

	override fun performAction() {
		if (continuation.isActive) continuation.resume(BubbleBarResult.ActionPerformed)
	}

	override fun dismiss() {
		if (continuation.isActive) continuation.resume(BubbleBarResult.Dismissed)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || this::class != other::class) return false

		other as BubbleBarDataImpl

		if (visuals != other.visuals) return false
		return continuation == other.continuation
	}

	override fun hashCode(): Int {
		var result = visuals.hashCode()
		result = 31 * result + continuation.hashCode()
		return result
	}

}

class BubbleBarHostState {

	/**
	 * Only one [BubbleBar] can be shown at a time. Since a suspending Mutex is a fair queue, this
	 * manages our message queue and we don't have to maintain one.
	 */
	private val mutex = Mutex()

	/**
	 * The current [BubbleBarData] being shown by the [BubbleBarHostState], or `null` if none.
	 */
	var currentBubbleBarData by mutableStateOf<BubbleBarData?>(null)
		private set

	suspend fun showBubble(
		message: String,
		actionLabel: String? = null,
		withDismissAction: Boolean = false,
		duration: BubbleBarDuration = if (actionLabel == null) BubbleBarDuration.Short
		else BubbleBarDuration.Indefinite
	): BubbleBarResult = showBubble(
		visuals = BubbleBarVisualsImpl(
			message = message,
			actionLabel = actionLabel,
			withDismissAction = withDismissAction,
			duration = duration
		)
	)

	suspend fun showBubble(visuals: BubbleBarVisuals): BubbleBarResult = mutex.withLock {
		try {
			return suspendCancellableCoroutine { continuation ->
				currentBubbleBarData = BubbleBarDataImpl(visuals, continuation)
			}
		} finally {
			currentBubbleBarData = null
		}
	}
}

internal fun BubbleBarDuration.toMillis(
	hasAction: Boolean,
	accessibilityManager: AccessibilityManager?
): Long {
	val original = when (this) {
		BubbleBarDuration.Long -> 8000L
		BubbleBarDuration.Short -> 4000L
		BubbleBarDuration.Indefinite -> Long.MAX_VALUE
	}

	if (accessibilityManager == null) {
		return original
	}

	return accessibilityManager.calculateRecommendedTimeoutMillis(
		original,
		containsIcons = true,
		containsText = true,
		containsControls = hasAction
	)
}

@Composable
fun rememberBubbleBarHostState(): BubbleBarHostState {
	return remember { BubbleBarHostState() }
}

object BubbleBarDefaults {

	/** Default shape of a bubble notification. */
	val shape: Shape @Composable get() = BubbleBarTokens.ContainerShape

	/** Default color of a bubble notification. */
	val color: Color @Composable get() = BubbleBarTokens.ContainerColor

	/** Default content color of a bubble notification. */
	val contentColor: Color @Composable get() = BubbleBarTokens.SupportingTextColor

	/** Default action color of a bubble notification. */
	val actionColor: Color @Composable get() = BubbleBarTokens.ActionLabelTextColor

	/** Default action content color of a bubble notification. */
	val actionContentColor: Color @Composable get() = BubbleBarTokens.ActionLabelTextColor

	/** Default dismiss action content color of a bubble notification. */
	val dismissActionContentColor: Color @Composable get() = BubbleBarTokens.IconColor

}
