package com.dcns.dailycost.ui.register

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.R
import com.dcns.dailycost.data.Constant
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.foundation.base.BaseScreenWrapper
import com.dcns.dailycost.foundation.extension.dailyCostColor
import com.dcns.dailycost.foundation.extension.toast
import com.dcns.dailycost.foundation.theme.DailyCostTheme
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.state.StateDialog
import com.maxkeppeler.sheets.state.models.ProgressIndicator
import com.maxkeppeler.sheets.state.models.State
import com.maxkeppeler.sheets.state.models.StateConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
	viewModel: RegisterViewModel,
	navigationActions: NavigationActions
) {

	val context = LocalContext.current

	val state by viewModel.state.collectAsStateWithLifecycle()

	val useCaseState = rememberUseCaseState(
		visible = false,
		onCloseRequest = {}
	)

	val stateDialogState = State.Loading(
		"Wait a moment",
		ProgressIndicator.Circular {
			CircularProgressIndicator(
				color = DailyCostTheme.colorScheme.primary,
				modifier = Modifier
					.padding(16.dp)
					.size(48.dp)
			)
		}
	)

	LaunchedEffect(state.isSuccess, state.isLoading) {
		if (state.isLoading) useCaseState.show() else useCaseState.hide()
		if (state.isSuccess) {
			context.getString(R.string.registration_success).toast(context)
			navigationActions.navigateTo(TopLevelDestinations.LoginRegister.login)
		}
	}

	BackHandler {
		navigationActions.navigateTo(
			destination = TopLevelDestinations.LoginRegister.login
		)
	}

	StateDialog(
		state = useCaseState,
		config = StateConfig(
			state = stateDialogState
		)
	)

	BaseScreenWrapper(
		viewModel = viewModel,
		topBar = {
			TopAppBar(
				title = {},
				navigationIcon = {
					IconButton(
						onClick = {
							navigationActions.navigateTo(TopLevelDestinations.LoginRegister.login)
						}
					) {
						Icon(
							painter = painterResource(id = R.drawable.ic_arrow_left),
							contentDescription = null
						)
					}
				}
			)
		}
	) { scaffoldPadding ->
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.padding(scaffoldPadding)
				.fillMaxSize()
		) {
			RegisterScreenContent(
				state = state,
				onShowPasswordCheckedChanged = { show ->
					viewModel.onAction(RegisterAction.UpdateShowPassword(show))
				},
				onEmailChanged = { s ->
					viewModel.onAction(RegisterAction.UpdateEmail(s))
				},
				onPasswordChanged = { s ->
					viewModel.onAction(RegisterAction.UpdatePassword(s))
				},
				onSignUpClicked = {
					viewModel.onAction(RegisterAction.SignUp(context))
				},
				onSignInClicked = {
					navigationActions.navigateTo(TopLevelDestinations.LoginRegister.login)
				},
				onUsernameChanged = { s ->
					viewModel.onAction(RegisterAction.UpdateUsername(s))
				},
				modifier = Modifier
					.fillMaxWidth(0.92f)
					.fillMaxHeight()
			)
		}
	}
}

@Composable
private fun RegisterScreenContent(
	state: RegisterState,
	modifier: Modifier = Modifier,
	onShowPasswordCheckedChanged: (Boolean) -> Unit = {},
	onUsernameChanged: (String) -> Unit = {},
	onPasswordChanged: (String) -> Unit = {},
	onSignInClicked: () -> Unit = {},
	onSignUpClicked: () -> Unit = {},
	onEmailChanged: (String) -> Unit = {}
) {

	val _32dp = dimensionResource(id = com.intuit.sdp.R.dimen._32sdp)

	val constraintSet = ConstraintSet {
		val (
			topContent,
			centerContent,
			bottomContent
		) = createRefsFor(
			"topContent",
			"centerContent",
			"bottomContent",
		)

		val gl1 = createGuidelineFromTop(_32dp)

		constrain(topContent) {
			start.linkTo(parent.start)
			top.linkTo(gl1)
		}

		constrain(centerContent) {
			centerHorizontallyTo(parent)

			top.linkTo(topContent.bottom)
			bottom.linkTo(bottomContent.top)
		}

		constrain(bottomContent) {
			centerHorizontallyTo(parent)

			top.linkTo(centerContent.bottom)
			bottom.linkTo(parent.bottom)
		}
	}

	ConstraintLayout(
		constraintSet = constraintSet,
		modifier = modifier
	) {
		TopContent(
			modifier = Modifier
				.layoutId("topContent")
		)

		CenterContent(
			email = state.email,
			password = state.password,
			username = state.username,
			emailError = state.emailError,
			passwordError = state.passwordError,
			usernameError = state.usernameError,
			showPassword = state.showPassword,
			onShowPasswordCheckedChanged = onShowPasswordCheckedChanged,
			onEmailChanged = onEmailChanged,
			onPasswordChanged = onPasswordChanged,
			onUsernameChanged = onUsernameChanged,
			modifier = Modifier
				.layoutId("centerContent")
		)

		BottomContent(
			onSignInClicked = onSignInClicked,
			onSignUpClicked = onSignUpClicked,
			modifier = Modifier
				.layoutId("bottomContent")
		)
	}
}

@Composable
private fun TopContent(
	modifier: Modifier = Modifier
) {

	Column(
		verticalArrangement = Arrangement.spacedBy(16.dp),
		modifier = modifier
	) {
		Text(
			text = stringResource(id = R.string.create_daily_cost_account),
			style = MaterialTheme.typography.titleLarge.copy(
				fontWeight = FontWeight.SemiBold
			)
		)

		Text(
			text = stringResource(id = R.string.unlock_all_the_features_to_manage_income_expenses),
			style = MaterialTheme.typography.titleMedium.copy(
				fontWeight = FontWeight.Normal,
				fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp,
				color = DailyCostTheme.colorScheme.labelText
			)
		)
	}
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CenterContent(
	email: String,
	password: String,
	username: String,
	emailError: String?,
	passwordError: String?,
	usernameError: String?,
	showPassword: Boolean,
	modifier: Modifier = Modifier,
	onShowPasswordCheckedChanged: (Boolean) -> Unit,
	onUsernameChanged: (String) -> Unit,
	onPasswordChanged: (String) -> Unit,
	onEmailChanged: (String) -> Unit,
) {

	val focusManager = LocalFocusManager.current

	val (
		emailFocusRequester,
		passwordFocusRequester,
		usernameFocusRequester
	) = FocusRequester.createRefs()

	Column(
		verticalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._12sdp)),
		modifier = modifier
	) {
		RegisterOutlinedTextField(
			text = username,
			label = stringResource(id = R.string.enter_name),
			errorText = usernameError,
			placeholderText = stringResource(id = R.string.username),
			focusRequester = usernameFocusRequester,
			onValueChanged = onUsernameChanged,
			keyboardOptions = KeyboardOptions(
				imeAction = ImeAction.Next,
				keyboardType = KeyboardType.Text
			),
			keyboardActions = KeyboardActions(
				onNext = {
					focusManager.moveFocus(FocusDirection.Next)
				}
			)
		)

		RegisterOutlinedTextField(
			text = email,
			label = stringResource(id = R.string.enter_email),
			errorText = emailError,
			placeholderText = stringResource(id = R.string.email),
			focusRequester = emailFocusRequester,
			onValueChanged = onEmailChanged,
			keyboardOptions = KeyboardOptions(
				imeAction = ImeAction.Next,
				keyboardType = KeyboardType.Email
			),
			keyboardActions = KeyboardActions(
				onNext = {
					focusManager.moveFocus(FocusDirection.Next)
				}
			)
		)

		RegisterOutlinedTextField(
			text = password,
			label = stringResource(id = R.string.enter_password),
			errorText = passwordError,
			placeholderText = stringResource(id = R.string.password),
			visualTransformation = if (!showPassword) PasswordVisualTransformation() else VisualTransformation.None,
			focusRequester = passwordFocusRequester,
			onValueChanged = onPasswordChanged,
			keyboardOptions = KeyboardOptions(
				imeAction = ImeAction.Done,
				keyboardType = KeyboardType.Password
			),
			keyboardActions = KeyboardActions(
				onNext = {
					focusManager.clearFocus()
				}
			),
			trailingIcon = {
				IconButton(
					onClick = {
						onShowPasswordCheckedChanged(!showPassword)
					}
				) {
					Icon(
						painter = painterResource(
							id = if (showPassword) R.drawable.ic_eye
							else R.drawable.ic_eye_slash
						),
						contentDescription = null
					)
				}
			},
		)
	}
}

@Composable
private fun BottomContent(
	modifier: Modifier = Modifier,
	onSignInClicked: () -> Unit,
	onSignUpClicked: () -> Unit
) {

	val uriHandler = LocalUriHandler.current

	val registerTouAndPpText = buildAnnotatedString {
		val tou = stringResource(id = R.string.terms_of_use)
		val pp = stringResource(id = R.string.privacy_policy)
		var text = stringResource(
			id = R.string.by_registering_txt,
			tou,
			pp
		)

		text = text.replace(tou, "\n $tou")

		withStyle(
			MaterialTheme.typography.titleSmall.copy(
				fontWeight = FontWeight.Normal,
				color = DailyCostTheme.colorScheme.text
			).toSpanStyle()
		) {
			append(text)
		}

		val startTou = text.indexOf(tou)
		val endTou = startTou + tou.length

		val startPp = text.indexOf(pp)
		val endPp = startPp + pp.length

		addStyle(
			end = endTou,
			start = startTou,
			style = MaterialTheme.typography.titleSmall.copy(
				fontWeight = FontWeight.SemiBold,
				color = DailyCostTheme.colorScheme.primary
			).toSpanStyle()
		)

		addStyle(
			end = endPp,
			start = startPp,
			style = MaterialTheme.typography.titleSmall.copy(
				fontWeight = FontWeight.SemiBold,
				color = DailyCostTheme.colorScheme.primary
			).toSpanStyle()
		)

		addStringAnnotation(
			tag = "tou",
			annotation = Constant.TERMS_OF_USE_URL,
			start = startTou,
			end = endTou
		)

		addStringAnnotation(
			tag = "pp",
			annotation = Constant.PRIVACY_POLICY_URL,
			start = startPp,
			end = endPp
		)
	}

	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(8.dp),
		modifier = modifier
	) {
		Button(
			shape = RoundedCornerShape(25),
			onClick = onSignUpClicked,
			contentPadding = PaddingValues(vertical = dimensionResource(id = com.intuit.sdp.R.dimen._12sdp)),
			colors = ButtonDefaults.buttonColors(
				containerColor = DailyCostTheme.colorScheme.primary
			),
			modifier = Modifier
				.fillMaxWidth()
		) {
			Text(stringResource(R.string.sign_up))
		}

		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(8.dp)
		) {
			Text(
				text = stringResource(id = R.string.already_have_an_account),
				style = MaterialTheme.typography.titleSmall.copy(
					fontWeight = FontWeight.Normal
				)
			)

			ClickableText(
				text = buildAnnotatedString {
					append(stringResource(id = R.string.sign_in))
				},
				style = MaterialTheme.typography.titleSmall.copy(
					fontWeight = FontWeight.SemiBold,
					color = DailyCostTheme.colorScheme.primary
				),
				onClick = {
					onSignInClicked()
				}
			)
		}

		Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._6sdp)))

		ClickableText(
			text = registerTouAndPpText,
			onClick = { offset ->
				registerTouAndPpText
					.getStringAnnotations("tou", offset, offset)
					.firstOrNull()?.let {
						uriHandler.openUri(it.item)

						return@ClickableText
					}

				registerTouAndPpText
					.getStringAnnotations("pp", offset, offset)
					.firstOrNull()?.let {
						uriHandler.openUri(it.item)
					}
			}
		)
	}
}

@Composable
private fun RegisterOutlinedTextField(
	text: String,
	label: String,
	placeholderText: String,
	focusRequester: FocusRequester,
	keyboardOptions: KeyboardOptions,
	keyboardActions: KeyboardActions,
	modifier: Modifier = Modifier,
	visualTransformation: VisualTransformation = VisualTransformation.None,
	errorText: String? = null,
	trailingIcon: @Composable (() -> Unit)? = null,
	onValueChanged: (String) -> Unit
) {
	Column(
		verticalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._8sdp)),
		modifier = modifier
	) {
		Text(
			text = label,
			style = MaterialTheme.typography.titleSmall.copy(
				fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value.sp
			)
		)

		OutlinedTextField(
			value = text,
			singleLine = true,
			isError = errorText != null,
			onValueChange = onValueChanged,
			shape = RoundedCornerShape(20),
			colors = OutlinedTextFieldDefaults.dailyCostColor(),
			visualTransformation = visualTransformation,
			keyboardOptions = keyboardOptions,
			keyboardActions = keyboardActions,
			trailingIcon = trailingIcon,
			placeholder = {
				Text(placeholderText)
			},
			supportingText = if (errorText != null) {
				{
					Text(errorText)
				}
			} else null,
			modifier = Modifier
				.fillMaxWidth()
				.focusRequester(focusRequester)
		)
	}
}
