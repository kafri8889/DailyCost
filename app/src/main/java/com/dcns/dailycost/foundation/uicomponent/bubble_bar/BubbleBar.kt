package com.dcns.dailycost.foundation.uicomponent.bubble_bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.RecomposeScope
import androidx.compose.runtime.State
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay

@Composable
fun BubbleBarHost(
	hostState: BubbleBarHostState,
	modifier: Modifier = Modifier,
	animationSpec: FiniteAnimationSpec<IntOffset> = tween(256),
	bubbleBar: @Composable (BubbleBarData) -> Unit = { BubbleBar(it) },
	content: @Composable () -> Unit
) {

	val currentBubbleBarData = hostState.currentBubbleBarData
	val accessibilityManager = LocalAccessibilityManager.current

	LaunchedEffect(currentBubbleBarData) {
		if (currentBubbleBarData != null) {
			val duration = currentBubbleBarData.visuals.duration.toMillis(
				hasAction = currentBubbleBarData.visuals.actionLabel != null,
				accessibilityManager = accessibilityManager
			)

			delay(duration)
			currentBubbleBarData.dismiss()
		}
	}

	Box {
		SlideInSlideOut(
			current = hostState.currentBubbleBarData,
			animationSpec = animationSpec,
			content = bubbleBar,
			modifier = modifier
				.zIndex(1f)
		)

		content()
	}
}

@Composable
private fun SlideInSlideOut(
	current: BubbleBarData?,
	modifier: Modifier = Modifier,
	animationSpec: FiniteAnimationSpec<IntOffset>,
	content: @Composable (BubbleBarData) -> Unit
) {
	val state = remember { SlideInSlideOutState<BubbleBarData?>() }

	if (current != state.current) {
		state.current = current
		val keys = state.items.map { it.key }.toMutableList()
		if (!keys.contains(current)) {
			keys.add(current)
		}
		state.items.clear()
		keys.filterNotNull().mapTo(state.items) { key ->
			SlideInSlideOutAnimationItem(key) { children ->
				val isVisible = key == current
				val duration = if (isVisible) BubbleBarFadeInMillis else BubbleBarFadeOutMillis
				val delay = BubbleBarFadeOutMillis + BubbleBarInBetweenDelayMillis
				val animationDelay = if (isVisible && keys.filterNotNull().size != 1) delay else 0

				val opacity = animatedOpacity(
					animation = tween(
						easing = LinearEasing,
						delayMillis = animationDelay,
						durationMillis = duration
					),
					visible = isVisible,
					onAnimationFinish = {

					}
				)

				LaunchedEffect(opacity) {
					if (key != state.current && opacity.value < 0.1f) {
						// leave only the current in the list
						state.items.removeAll { it.key == key }
						state.scope?.invalidate()
					}
				}

				AnimatedVisibility(
					visible = opacity.value > 0.1,
					enter = slideInVertically(
						animationSpec = animationSpec
					),
					exit = slideOutVertically(
						animationSpec = animationSpec
					)
				) {
					Box(
						Modifier
							.semantics {
								liveRegion = LiveRegionMode.Polite
								dismiss { key.dismiss(); true }
							}
					) {
						children()
					}
				}
			}
		}
	}

	Box(modifier) {
		state.scope = currentRecomposeScope
		state.items.forEach { (item, opacity) ->
			key(item) {
				opacity {
					content(item!!)
				}
			}
		}
	}
}

@Composable
private fun animatedOpacity(
	animation: AnimationSpec<Float>,
	visible: Boolean,
	onAnimationFinish: () -> Unit = {}
): State<Float> {
	val alpha = remember { Animatable(if (!visible) 1f else 0f) }
	LaunchedEffect(visible) {
		alpha.animateTo(
			if (visible) 1f else 0f,
			animationSpec = animation
		)
		onAnimationFinish()
	}
	return alpha.asState()
}

private class SlideInSlideOutState<T> {
	// we use Any here as something which will not be equals to the real initial value
	var current: Any? = Any()
	var items = mutableListOf<SlideInSlideOutAnimationItem<T>>()
	var scope: RecomposeScope? = null
}

private data class SlideInSlideOutAnimationItem<T>(
	val key: T,
	val transition: SlideInSlideOutTransition
)

private typealias SlideInSlideOutTransition = @Composable (content: @Composable () -> Unit) -> Unit

@Composable
fun BubbleBar(
	modifier: Modifier = Modifier,
	shape: Shape = BubbleBarDefaults.shape,
	containerColor: Color = BubbleBarDefaults.color,
	contentColor: Color = BubbleBarDefaults.contentColor,
	actionContentColor: Color = BubbleBarDefaults.actionContentColor,
	dismissActionContentColor: Color = BubbleBarDefaults.dismissActionContentColor,
	dismissAction: @Composable (() -> Unit)? = null,
	action: @Composable (() -> Unit)? = null,
	text: @Composable () -> Unit
) {

	Surface(
		shape = shape,
		color = containerColor,
		contentColor = contentColor,
		shadowElevation = BubbleBarTokens.ContainerElevation,
		modifier = Modifier
			.padding(OuterPadding)
			.then(modifier),
	) {

		val textStyle = BubbleBarTokens.SupportingTextFont
		val actionTextStyle = BubbleBarTokens.ActionLabelTextFont

		CompositionLocalProvider(LocalTextStyle provides textStyle) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.widthIn(max = ContainerMaxWidth)
					.heightIn(max = ContainerMaxHeight)
					.padding(
						if (dismissAction != null) InnerPaddingWithAction
						else InnerPadding
					)
			) {
				Box(modifier = Modifier.weight(1f)) {
					text()
				}

				if (action != null) {
					CompositionLocalProvider(
						LocalTextStyle provides actionTextStyle,
						LocalContentColor provides actionContentColor
					) {
						action()
					}
				}

				if (dismissAction != null) {
					CompositionLocalProvider(
						LocalContentColor provides dismissActionContentColor
					) {
						dismissAction()
					}
				}
			}
		}
	}

}

@Composable
fun BubbleBar(
	bubbleBarData: BubbleBarData,
	modifier: Modifier = Modifier,
	shape: Shape = BubbleBarDefaults.shape,
	containerColor: Color = BubbleBarDefaults.color,
	contentColor: Color = BubbleBarDefaults.contentColor,
	actionColor: Color = BubbleBarDefaults.actionColor,
	actionContentColor: Color = BubbleBarDefaults.actionContentColor,
	dismissActionContentColor: Color = BubbleBarDefaults.dismissActionContentColor
) {

	val actionComposable: (@Composable () -> Unit)? = if (bubbleBarData.visuals.actionLabel != null) {
		@Composable {
			TextButton(
				colors = ButtonDefaults.textButtonColors(contentColor = actionColor),
				onClick = { bubbleBarData.performAction() },
				content = { Text(bubbleBarData.visuals.actionLabel!!) }
			)
		}
	} else null

	val dismissActionComposable: (@Composable () -> Unit)? = if (bubbleBarData.visuals.withDismissAction) {
		@Composable {
			IconButton(
				onClick = { bubbleBarData.dismiss() },
				content = {
					Icon(
						imageVector = Icons.Rounded.Close,
						contentDescription = null,
					)
				}
			)
		}
	} else null

	BubbleBar(
		shape = shape,
		containerColor = containerColor,
		contentColor = contentColor,
		actionContentColor = actionContentColor,
		dismissActionContentColor = dismissActionContentColor,
		modifier = modifier,
		action = actionComposable,
		dismissAction = dismissActionComposable,
		text = { Text(bubbleBarData.visuals.message) }
	)
}

private const val BubbleBarFadeInMillis = 150
private const val BubbleBarFadeOutMillis = 75
private const val BubbleBarInBetweenDelayMillis = 0

private val ContainerMaxWidth = 600.dp
private val ContainerMaxHeight = 128.dp
private val OuterPadding = 16.dp
private val InnerPadding = 12.dp
private val InnerPaddingWithAction = 8.dp
