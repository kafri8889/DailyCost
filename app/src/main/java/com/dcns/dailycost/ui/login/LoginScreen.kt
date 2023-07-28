package com.dcns.dailycost.ui.login

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
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
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.MainActivity
import com.dcns.dailycost.R
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.Status
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
import timber.log.Timber

@Preview(showSystemUi = true)
@Composable
private fun LoginScreenContentPreview() {
    DailyCostTheme {
        LoginScreenContent(state = LoginState())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
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

    LaunchedEffect(Unit) {
        // called on first recomposition
        viewModel.onAction(LoginAction.ClearData(context))
    }

    LaunchedEffect(state.resource) {
        when (state.resource?.status) {
            Status.Success -> {
                Timber.i("Login success")

                context.getString(R.string.login_success).toast(context)

                useCaseState.hide()

                navigationActions.navigateTo(
                    destination = TopLevelDestinations.Home.dashboard,
                    builder = NavigationActions.defaultNavOptionsBuilder(
                        popTo = TopLevelDestinations.Home.dashboard
                    )
                )
            }
            Status.Error -> {
                Timber.i("Login error: ${state.resource?.message}")

                state.resource?.message.toast(context, Toast.LENGTH_LONG)

                useCaseState.hide()
            }
            Status.Loading -> {
                useCaseState.show()
            }
            else -> {}
        }
    }

    StateDialog(
        state = useCaseState,
        config = StateConfig(
            state = stateDialogState
        )
    )

    BackHandler {
        if (state.isFirstInstall) {
            navigationActions.navigateTo(TopLevelDestinations.Onboarding.onboarding)
        } else (context as MainActivity).finishAndRemoveTask()
    }

    BaseScreenWrapper(
        viewModel = viewModel,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigationActions.navigateTo(TopLevelDestinations.Onboarding.onboarding)
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
            LoginScreenContent(
                state = state,
                onShowPasswordCheckedChanged = { show ->
                    viewModel.onAction(LoginAction.UpdateShowPassword(show))
                },
                onRememberMeCheckedChanged = { remember ->
                    viewModel.onAction(LoginAction.UpdateRememberMe(remember))
                },
                onEmailChanged = { s ->
                    viewModel.onAction(LoginAction.UpdateEmail(s))
                },
                onPasswordChanged = { s ->
                    viewModel.onAction(LoginAction.UpdatePassword(s))
                },
                onSignInClicked = {
                    viewModel.onAction(LoginAction.SignIn(context))
                },
                onSignUpClicked = {
                    navigationActions.navigateTo(TopLevelDestinations.LoginRegister.register)
                },
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
private fun LoginScreenContent(
    state: LoginState,
    modifier: Modifier = Modifier,
    onShowPasswordCheckedChanged: (Boolean) -> Unit = {},
    onRememberMeCheckedChanged: (Boolean) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onSignInClicked: () -> Unit = {},
    onSignUpClicked: () -> Unit = {},
    onEmailChanged: (String) -> Unit = {}
) {

    val _48dp = dimensionResource(id = com.intuit.sdp.R.dimen._48sdp)

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

        val gl1 = createGuidelineFromTop(_48dp)

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
            rememberMe = state.rememberMe,
            emailError = state.emailError,
            passwordError = state.passwordError,
            showPassword = state.showPassword,
            onShowPasswordCheckedChanged = onShowPasswordCheckedChanged,
            onRememberMeCheckedChanged = onRememberMeCheckedChanged,
            onEmailChanged = onEmailChanged,
            onPasswordChanged = onPasswordChanged,
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
            text = stringResource(id = R.string.sign_in_to_daily_cost),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold
            )
        )

        Text(
            text = stringResource(id = R.string.record_all_your_financial_activities_anywhere),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Normal,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._16ssp).value.sp
            )
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CenterContent(
    email: String,
    password: String,
    rememberMe: Boolean,
    emailError: String?,
    passwordError: String?,
    showPassword: Boolean,
    modifier: Modifier = Modifier,
    onShowPasswordCheckedChanged: (Boolean) -> Unit,
    onRememberMeCheckedChanged: (Boolean) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
) {

    val focusManager = LocalFocusManager.current

    val (
        emailFocusRequester,
        passwordFocusRequester
    ) = FocusRequester.createRefs()

    val rememberMeConstraintSet = ConstraintSet {
        val (
            checkbox,
            titleText,
            bodyText
        ) = createRefsFor(
            "checkbox",
            "titleText",
            "bodyText"
        )

        constrain(checkbox) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        }

        constrain(titleText) {
            top.linkTo(checkbox.top)
            bottom.linkTo(checkbox.bottom)
            start.linkTo(checkbox.end, 8.dp)
        }

        constrain(bodyText) {
            top.linkTo(titleText.bottom, 8.dp)
            start.linkTo(checkbox.end, 8.dp)
            end.linkTo(parent.end)

            width = Dimension.fillToConstraints
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = com.intuit.sdp.R.dimen._12sdp)),
        modifier = modifier
    ) {
        LoginOutlinedTextField(
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

        LoginOutlinedTextField(
            text = password,
            label = stringResource(id = R.string.enter_password),
            errorText = passwordError,
            placeholderText = stringResource(id = R.string.password),
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

        Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._8sdp)))

        Text(
            textAlign = TextAlign.End,
            text = stringResource(id = R.string.forgot_password),
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium,
                fontSize = dimensionResource(id = com.intuit.ssp.R.dimen._12ssp).value.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._8sdp)))

        ConstraintLayout(
            constraintSet = rememberMeConstraintSet,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Checkbox
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(2.dp)
                    .size(20.dp)
                    .clip(RoundedCornerShape(30))
                    .border(
                        width = 1.dp,
                        color = if (rememberMe) Color.Transparent else DailyCostTheme.colorScheme.outline,
                        shape = RoundedCornerShape(30)
                    )
                    .background(
                        color = if (rememberMe) DailyCostTheme.colorScheme.primary
                        else Color.White
                    )
                    .clip(CircleShape)
                    .minimumInteractiveComponentSize()
                    .clickable {
                        onRememberMeCheckedChanged(!rememberMe)
                    }
                    .layoutId("checkbox")
            ) {
                if (rememberMe) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = DailyCostTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .size(16.dp)
                    )
                }
            }
            
            Text(
                text = stringResource(id = R.string.keep_me_signed_in),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
                    .layoutId("titleText")
            )

            Text(
                text = stringResource(id = R.string.by_checking_this_box_you_wont_to_sign),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = DailyCostTheme.colorScheme.text
                ),
                modifier = Modifier
                    .layoutId("bodyText")
            )
        }
    }
}

@Composable
private fun BottomContent(
    modifier: Modifier = Modifier,
    onSignInClicked: () -> Unit,
    onSignUpClicked: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Button(
            shape = RoundedCornerShape(25),
            onClick = onSignInClicked,
            colors = ButtonDefaults.buttonColors(
                containerColor = DailyCostTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(stringResource(R.string.sign_in))
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = com.intuit.sdp.R.dimen._6sdp)))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.dont_have_an_account),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Normal
                ),
            )

            ClickableText(
                text = buildAnnotatedString {
                    append(stringResource(id = R.string.sign_up))
                },
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = DailyCostTheme.colorScheme.primary
                ),
                onClick = {
                    onSignUpClicked()
                }
            )
        }
    }
}

@Composable
private fun LoginOutlinedTextField(
    text: String,
    label: String,
    placeholderText: String,
    focusRequester: FocusRequester,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    modifier: Modifier = Modifier,
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
