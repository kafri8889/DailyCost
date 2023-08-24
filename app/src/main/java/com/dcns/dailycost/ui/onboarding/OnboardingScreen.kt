package com.dcns.dailycost.ui.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.MainActivity
import com.dcns.dailycost.R
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.foundation.theme.DailyCostTheme
import com.dcns.dailycost.foundation.uicomponent.LinearProgressIndicator

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, showSystemUi = true, device = "id:J2 Prime")
//@Preview(showBackground = true, showSystemUi = true, device = "spec:width=1280dp,height=800dp,dpi=240")
@Preview(
	showBackground = true,
	showSystemUi = true,
	device = "spec:width=360dp,height=700dp,dpi=320"
)
@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=900dp")
@Composable
private fun OnboardingScreenContentPreview() {

	val config = LocalConfiguration.current
	val density = LocalDensity.current

	DailyCostTheme {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center,
			modifier = Modifier
				.fillMaxSize()
		) {
			OnboardingScreenContent(
				progress = { 0.5f },
				bodyText = stringResource(id = R.string.you_can_see_where_the_money_goes),
				titleText = stringResource(id = R.string.you_can_see_where_the_money_goes),
				primaryButtonText = "Next",
				secondaryButtonText = "Skip",
				onPrimaryButtonClicked = {},
				onSecondaryButtonClicked = {},
				windowSizeClass = WindowSizeClass.calculateFromSize(
					DpSize(
						config.screenWidthDp.dp,
						config.screenHeightDp.dp
					)
				),
				modifier = Modifier
					.fillMaxSize(0.92f)
			)
		}
	}
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun OnboardingScreen(
	viewModel: OnboardingViewModel,
	navigationActions: NavigationActions
) {
	val context = LocalContext.current

	val state by viewModel.state.collectAsStateWithLifecycle()

	val windowSizeClass = calculateWindowSizeClass(context as MainActivity)

	BackHandler {
		if (state.currentPage != 1) {
			viewModel.onAction(OnboardingAction.UpdateCurrentPage(state.currentPage - 1))

			return@BackHandler
		}

		context.finishAndRemoveTask()
	}

	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
		modifier = Modifier
			.fillMaxSize()
			.systemBarsPadding()
	) {
		OnboardingScreenContent(
			progress = { state.currentPage / state.pageCount.toFloat() },
			bodyText = stringResource(id = state.bodyText),
			titleText = stringResource(id = state.titleText),
			primaryButtonText = stringResource(id = state.primaryButtonText),
			secondaryButtonText = stringResource(id = state.secondaryButtonText),
			windowSizeClass = windowSizeClass,
			onPrimaryButtonClicked = {
				if (state.currentPage == state.pageCount) {
					// Sign in
					navigationActions.navigateTo(TopLevelDestinations.LoginRegister.login)
					return@OnboardingScreenContent
				}

				viewModel.onAction(OnboardingAction.UpdateCurrentPage(state.currentPage + 1))
			},
			onSecondaryButtonClicked = {
				if (state.currentPage == state.pageCount) {
					// Sign up
					navigationActions.navigateTo(TopLevelDestinations.LoginRegister.register)
					return@OnboardingScreenContent
				}

				viewModel.onAction(OnboardingAction.UpdateCurrentPage(state.pageCount))
			},
			modifier = Modifier
				.fillMaxSize(0.92f)
		)
	}
}

@Composable
private fun OnboardingScreenContent(
	progress: () -> Float,
	bodyText: String,
	titleText: String,
	primaryButtonText: String,
	secondaryButtonText: String,
	windowSizeClass: WindowSizeClass,
	modifier: Modifier = Modifier,
	onPrimaryButtonClicked: () -> Unit,
	onSecondaryButtonClicked: () -> Unit
) {

	val config = LocalConfiguration.current

	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier
			.fillMaxSize()
	) {
		OnboardingLinearProgressIndicator(
			progress = progress,
			modifier = Modifier
				.clip(CircleShape)
				.fillMaxWidth()
				.height(8.dp)
		)

		Spacer(
			modifier = Modifier
				.height(
					dimensionResource(
						id = if (windowSizeClass.heightSizeClass == WindowHeightSizeClass.Expanded) com.intuit.sdp.R.dimen._32sdp
						else com.intuit.sdp.R.dimen._20sdp
					)
				)
		)

		Image(
			painter = ColorPainter(Color.LightGray),
			contentDescription = null,
			modifier = Modifier
				.width(config.smallestScreenWidthDp.dp - 16.dp)
				.aspectRatio(
					if (windowSizeClass.heightSizeClass == WindowHeightSizeClass.Expanded || config.screenHeightDp >= 700) 1f
					else 1f / 0.8f
				)
		)

		Spacer(
			modifier = Modifier
				.height(
					dimensionResource(
						id = if (windowSizeClass.heightSizeClass == WindowHeightSizeClass.Expanded) com.intuit.sdp.R.dimen._28sdp
						else com.intuit.sdp.R.dimen._20sdp
					)
				)
		)

		Text(
			text = titleText,
			textAlign = TextAlign.Center,
			style = MaterialTheme.typography.titleMedium.copy(
				fontWeight = FontWeight.Bold,
				fontSize = dimensionResource(
					id = when {
						config.screenHeightDp < 700 -> com.intuit.ssp.R.dimen._16ssp
						windowSizeClass.heightSizeClass == WindowHeightSizeClass.Expanded -> com.intuit.ssp.R.dimen._20ssp
						else -> com.intuit.ssp.R.dimen._18ssp
					}
				).value.sp
			),
			modifier = Modifier
				.animateContentSize(tween(256))
		)

		Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._16sdp)))

		Text(
			text = bodyText,
			textAlign = TextAlign.Center,
			style = MaterialTheme.typography.titleMedium.copy(
				fontWeight = FontWeight.Normal,
				fontSize = dimensionResource(
					id = when {
						config.screenHeightDp < 700 -> com.intuit.ssp.R.dimen._12ssp
						windowSizeClass.heightSizeClass == WindowHeightSizeClass.Expanded -> com.intuit.ssp.R.dimen._16ssp
						else -> com.intuit.ssp.R.dimen._14ssp
					}
				).value.sp
			),
			modifier = Modifier
				.animateContentSize(tween(256))
		)

		Spacer(
			modifier = Modifier
				.height(
					dimensionResource(
						id = if (windowSizeClass.heightSizeClass == WindowHeightSizeClass.Expanded) com.intuit.sdp.R.dimen._32sdp
						else com.intuit.sdp.R.dimen._20sdp
					)
				)
		)

		OnboardingPrimaryButton(
			text = primaryButtonText,
			onClick = onPrimaryButtonClicked,
			modifier = Modifier
				.fillMaxWidth()
		)

		Spacer(
			modifier = Modifier
				.height(
					dimensionResource(
						id = if (windowSizeClass.heightSizeClass == WindowHeightSizeClass.Expanded) com.intuit.sdp.R.dimen._16sdp
						else com.intuit.sdp.R.dimen._10sdp
					)
				)
		)

		OnboardingSecondaryButton(
			text = secondaryButtonText,
			onClick = onSecondaryButtonClicked,
			progress = progress(),
			modifier = Modifier
				.fillMaxWidth()
		)
	}
}

@Composable
private fun OnboardingLinearProgressIndicator(
	progress: () -> Float,
	modifier: Modifier = Modifier
) {
	LinearProgressIndicator(
		progress = progress(),
		color = DailyCostTheme.colorScheme.primary,
		trackColor = DailyCostTheme.colorScheme.primary.copy(alpha = 0.3f), // Opacity 30%
		modifier = modifier
	)
}

@Composable
private fun OnboardingPrimaryButton(
	text: String,
	modifier: Modifier = Modifier,
	onClick: () -> Unit = {}
) {
	val config = LocalConfiguration.current

	Button(
		contentPadding = PaddingValues(
			dimensionResource(
				id = if (config.screenHeightDp < 700) com.intuit.sdp.R.dimen._8sdp
				else com.intuit.sdp.R.dimen._12sdp
			)
		),
		shape = RoundedCornerShape(25),
		onClick = onClick,
		modifier = modifier,
		colors = ButtonDefaults.buttonColors(
			containerColor = DailyCostTheme.colorScheme.primary
		)
	) {
		Text(
			text = text,
			style = LocalTextStyle.current.copy(
				fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp
			)
		)
	}
}

@Composable
private fun OnboardingSecondaryButton(
	text: String,
	progress: Float,
	modifier: Modifier = Modifier,
	onClick: () -> Unit = {}
) {
	val config = LocalConfiguration.current

	TextButton(
		contentPadding = PaddingValues(
			dimensionResource(
				id = if (config.screenHeightDp < 700) com.intuit.sdp.R.dimen._8sdp
				else com.intuit.sdp.R.dimen._12sdp
			)
		),
		shape = RoundedCornerShape(25),
		onClick = onClick,
		border = BorderStroke(
			width = 1.dp,
			color = if (progress >= 0.99f) DailyCostTheme.colorScheme.primary
			else Color.Transparent
		),
		modifier = modifier,
		colors = ButtonDefaults.textButtonColors(
			contentColor = DailyCostTheme.colorScheme.text
		)
	) {
		Text(
			text = text,
			style = LocalTextStyle.current.copy(
				fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp
			)
		)
	}
}
